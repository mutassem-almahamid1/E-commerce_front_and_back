package com.mutassemalmahamid.ecommerce.config;

import com.mutassemalmahamid.ecommerce.handelException.exception.BadReqException;
import com.mutassemalmahamid.ecommerce.security.JwtDatabaseAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDatabaseAuthenticationFilter jwtDatabaseAuthenticationFilter;

    private static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/book/top").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/book").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/book/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/book/category").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/book/search/page").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/book/search").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/book/base").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/category/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/category").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/category/page").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/user/{userId}/want-to-read").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/user/{userId}/finished").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/user/{userId}/currently-reading").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/user/{id}/following").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/user/{id}/followers").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/user/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/paged/user/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/book/{bookId}").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler())
                                .authenticationEntryPoint(errorAuthenticationEntryPoint())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtDatabaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://orange-wave-067979203.1.azurestaticapps.net"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN,
                        String.format("Access Denied: [%s] You do not have permission to access this resource.", request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous"));
    }

    public AuthenticationEntryPoint errorAuthenticationEntryPoint() {
        return (request, response, authException) ->
                writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    private void writeJsonResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"" + message + "\"}");
        } catch (Exception e) {
            throw new BadReqException("Failed to write JSON response, msg: " + e.getMessage());
        }
    }

}