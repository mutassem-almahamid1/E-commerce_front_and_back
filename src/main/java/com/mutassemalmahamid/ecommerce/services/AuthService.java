package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.TokenRefreshRequest;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserLoginReq;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserSignUpRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.JwtResponse;
import com.mutassemalmahamid.ecommerce.model.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    @Transactional
    UserResponse registerUser(UserSignUpRequest signupRequest);

    @Transactional
    JwtResponse authenticateUser(UserLoginReq loginRequest, HttpServletResponse response);

    void addTokenToHeader(HttpServletResponse response, String jwtToken, String refreshToken);

    @Transactional
    JwtResponse refreshToken(TokenRefreshRequest request);

    @Transactional
    void logoutUser(String email, @NotNull HttpServletRequest request, HttpServletResponse response);
}
