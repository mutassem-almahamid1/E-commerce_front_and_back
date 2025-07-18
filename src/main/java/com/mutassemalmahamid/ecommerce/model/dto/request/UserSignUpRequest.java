package com.mutassemalmahamid.ecommerce.model.dto.request;

import com.mutassemalmahamid.ecommerce.model.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpRequest {

    @NotBlank(message = "the username is required")
    @Size(min = 4,max = 50,message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String image;

    @NotBlank(message = "Location is required")
    private String location;

}
