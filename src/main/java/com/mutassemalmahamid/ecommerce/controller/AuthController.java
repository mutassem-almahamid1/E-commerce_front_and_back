package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.model.dto.request.TokenRefreshRequest;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserLoginReq;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserSignUpRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.JwtResponse;
import com.mutassemalmahamid.ecommerce.model.dto.response.UserResponse;
import com.mutassemalmahamid.ecommerce.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     *
     * @param signupRequest the signup request
     * @return response entity with user DTO
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserSignUpRequest signupRequest) {
        UserResponse userDto = authService.registerUser(signupRequest);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Authenticate user
     *
     * @param loginRequest the login request
     * @return response entity with JWT response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserLoginReq loginRequest, HttpServletResponse response) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest, response);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Refresh token
     *
     * @param request the token refresh request
     * @return response entity with JWT response
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        JwtResponse jwtResponse = authService.refreshToken(request);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Refresh token (alternative endpoint for frontend)
     *
     * @return response entity with JWT response
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshTokenSimple(HttpServletRequest request, HttpServletResponse response) {
        // Extract refresh token from cookies
        String refreshToken = extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken(refreshToken);

        JwtResponse jwtResponse = authService.refreshToken(refreshRequest);

        // Add new tokens to cookies
        authService.addTokenToHeader(response, jwtResponse.getToken(), jwtResponse.getRefreshToken());

        return ResponseEntity.ok(jwtResponse);
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Logout user
     *
     * @return response entity with success message
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(@NotNull HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        authService.logoutUser(userDetails.getUsername(), request, response);
        return ResponseEntity.ok(AssistantHelper.toMessageResponse("User logged out successfully"));
    }

    /**
     * Get current authenticated user
     *
     * @return response entity with current user details
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponse currentUser = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(currentUser);
    }

}