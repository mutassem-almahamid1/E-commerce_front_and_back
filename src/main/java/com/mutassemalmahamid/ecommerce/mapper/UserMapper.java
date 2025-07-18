package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.document.User;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserLoginReq;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserSignUpRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.UserResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Roles;
import com.mutassemalmahamid.ecommerce.model.enums.Status;

import java.time.LocalDateTime;

public class UserMapper {

    public static User toEntity(UserSignUpRequest userSignUpRequest) {
        return User.builder()
                .username(AssistantHelper.trimString(userSignUpRequest.getUsername()))
                .email(AssistantHelper.trimString(userSignUpRequest.getEmail()))
                .password(AssistantHelper.trimString(userSignUpRequest.getPassword()))
                .image(AssistantHelper.trimString(userSignUpRequest.getImage()))
                .location(AssistantHelper.trimString(userSignUpRequest.getLocation()))
                .role(Roles.USER)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User toEntity(UserLoginReq userLoginReq) {
        return User.builder()
                .email(AssistantHelper.trimString(userLoginReq.getEmail()))
                .password(userLoginReq.getPassword())
                .build();
    }

    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .image(user.getImage())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .location(user.getLocation())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    public static void updateEntity(User user, UserSignUpRequest userSignUpRequest) {
        user.setUsername(AssistantHelper.trimString(userSignUpRequest.getUsername()));
        user.setEmail(AssistantHelper.trimString(userSignUpRequest.getEmail()));
        user.setImage(AssistantHelper.trimString(userSignUpRequest.getImage()));
        user.setLocation(AssistantHelper.trimString(userSignUpRequest.getLocation()));
        user.setUpdatedAt(LocalDateTime.now());
    }
}
