package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.BadCredentialsException;
import com.mutassemalmahamid.ecommerce.handelException.exception.BadReqException;
import com.mutassemalmahamid.ecommerce.handelException.exception.ConflictException;
import com.mutassemalmahamid.ecommerce.mapper.UserMapper;
import com.mutassemalmahamid.ecommerce.model.document.User;
import com.mutassemalmahamid.ecommerce.model.dto.request.TokenRefreshRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.JwtResponse;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserLoginReq;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserSignUpRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.UserResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.UserRepo;
import com.mutassemalmahamid.ecommerce.security.JwtUtils;
import com.mutassemalmahamid.ecommerce.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Service for authentication operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${app.jwt.cookie-expiration}")
    private int cookieExpiration;

    /**
     * Register a new user
     *
     * @param signupRequest the signup request
     * @return the created user DTO
     */
    @Transactional
    @Override
    public UserResponse registerUser(UserSignUpRequest signupRequest) {
        // Check if username is already taken
        if (userRepository.getByUsernameIfPresent(signupRequest.getUsername()).isPresent()) {
            throw new ConflictException("Username is already taken");
        }

        // Check if email is already in use
        if (userRepository.getByEmailIfPresent(signupRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email is already in use");
        }

        // Create new user
        User user = UserMapper.toEntity(signupRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);


        return UserMapper.toResponse(savedUser);
    }

    /**
     * Authenticate user and generate JWT token
     *
     * @param loginRequest the login request
     * @return JWT response with tokens and user details
     */
    @Transactional
    @Override
    public JwtResponse authenticateUser(UserLoginReq loginRequest, HttpServletResponse response) {
        // Authenticate user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail().trim(), loginRequest.getPassword().trim()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user = userRepository.getByEmail(loginRequest.getEmail().trim());
        if (user.getStatus() == Status.BLOCKED) {
            throw new BadReqException("Your account is blocked. Connect with support.");
        }

        if (!this.passwordEncoder.matches(loginRequest.getPassword().trim(), user.getPassword())) {
            throw new BadReqException("Incorrect Password.");
        }

        // Get user details
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

        // Generate JWT token
        String jwt = jwtUtils.generateJwtTokenWithRoles(user.getEmail(), List.of(user.getRole().name()));

        // Generate refresh token
        String refreshToken = jwtUtils.generateRefreshTokenFromUsername(user.getEmail());

        // Save refresh token to user
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiryDate(LocalDateTime.now().plusDays(7)); // 7 days expiry
        userRepository.save(user);

        addTokenToHeader(response, jwt, refreshToken);

        return JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(Collections.singletonList(user.getRole().name()))
                .build();
    }

    @Override
    public void addTokenToHeader(HttpServletResponse response, String jwtToken, String refreshToken) {
        Cookie cookie = new Cookie("access_token", jwtToken);
        cookie.setMaxAge(cookieExpiration);
        cookie.setPath("/");
        response.addCookie(cookie);

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setMaxAge(cookieExpiration);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
    }

    /**
     * Refresh JWT token using refresh token
     *
     * @param request the token refresh request
     * @return JWT response with new tokens
     */
    @Transactional
    @Override
    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        // Find user by refresh token
        User user = userRepository.getByRefreashToken(requestRefreshToken);

        // Check if refresh token is expired
        if (user.getRefreshTokenExpiryDate().isBefore(LocalDateTime.now())) {
            user.setRefreshToken(null);
            user.setRefreshTokenExpiryDate(null);
            userRepository.save(user);
            throw new BadCredentialsException("Refresh token was expired. Please make a new login request");
        }

        // Generate new JWT token
        String newToken = jwtUtils.generateTokenFromUsername(user.getUsername());

        // Generate new refresh token
        String newRefreshToken = jwtUtils.generateRefreshTokenFromUsername(user.getUsername());

        // Update refresh token in database
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiryDate(LocalDateTime.now().plusDays(7)); // 7 days expiry
        userRepository.save(user);

        // Get user roles
        List<String> roles = List.of(user.getRole().name());

        return JwtResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    /**
     * Logout user
     *
     * @param email the email
     */
    @Transactional
    @Override
    public void logoutUser(String email, @NotNull HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.getByEmail(email);

        // Clear refresh token
        user.setRefreshToken(null);
        user.setRefreshTokenExpiryDate(null);
        userRepository.save(user);

        // Retrieve all cookies from the request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Log cookie details for debugging
                log.info("Clearing cookie: {}", cookie.getName());

                // Set maxAge to 0 to invalidate the cookie
                cookie.setMaxAge(0);
                cookie.setPath("/"); // Ensure the cookie is removed across the entire app
                response.addCookie(cookie);
            }
        } else {
            log.info("No cookies found to clear.");
//            throw new BadReqException("No cookies found to clear.");
        }

        // Remove the Authorization header
        response.setHeader(HttpHeaders.AUTHORIZATION, "");
        SecurityContextHolder.clearContext();
    }

    /**
     * Get current user details by email
     *
     * @param email the user's email
     * @return UserResponse with current user details
     */
    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.getByEmail(email);
        return UserMapper.toResponse(user);
    }
}
