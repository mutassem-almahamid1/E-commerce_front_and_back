package com.mutassemalmahamid.ecommerce.model.dto.request;

@Data
public class UserSignUpRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
