package com.mutassemalmahamid.ecommerce.model.document;

import com.mutassemalmahamid.ecommerce.model.enums.Roles;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String image;
    private String location;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiryDate;

    private Roles role=Roles.USER;
    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
