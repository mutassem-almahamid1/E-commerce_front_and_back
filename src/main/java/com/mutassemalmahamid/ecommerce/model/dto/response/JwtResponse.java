package com.mutassemalmahamid.ecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type;
    private String id;
    private String username;
    private String email;
    private List<String> roles;
}