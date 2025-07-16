package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.UserLoginReq;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserSignUpRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.UserResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse getById(String id);
    MessageResponse updateStatus(String id, Status status);
    UserResponse getByUsername(String username);
    Page<UserResponse> getByNamePage(String name, Pageable pageable);
    Page<UserResponse> getByAllPage(int page, int size);
    MessageResponse update(String id, UserSignUpRequest request);
    MessageResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);

}
