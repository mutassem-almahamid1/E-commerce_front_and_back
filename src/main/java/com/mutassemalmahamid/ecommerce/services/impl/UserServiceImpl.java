package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.BadCredentialsException;
import com.mutassemalmahamid.ecommerce.handelException.exception.ConflictException;
import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.UserMapper;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.model.document.User;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserLoginReq;
import com.mutassemalmahamid.ecommerce.model.dto.request.UserSignUpRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.UserResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.UserRepo;
import com.mutassemalmahamid.ecommerce.services.ReviewService;
import com.mutassemalmahamid.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    private final MongoTemplate mongoTemplate;

    private final ReviewService reviewService;

    public UserServiceImpl(UserRepo userRepo, MongoTemplate mongoTemplate, ReviewService reviewService) {
        this.userRepo = userRepo;
        this.mongoTemplate = mongoTemplate;
        this.reviewService = reviewService;
    }



    @Override
    public UserResponse getById(String id) {
        User user = userRepo.getById(id);
        return UserMapper.toResponse(user);
    }

    @Override
    public MessageResponse updateStatus(String id, Status status) {
        User user = userRepo.getByIdAndStatusNot(id, Status.DELETED);
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        this.userRepo.save(user);
        return AssistantHelper.toMessageResponse("Updated Successfully.");
    }

    @Override
    public UserResponse getByUsername(String username) {
        User user = userRepo.getByUsername(username);
        return UserMapper.toResponse(user);
    }


    @Override
    public Page<UserResponse> getByNamePage(String name, Pageable pageable) {
        Page<User> userPage = userRepo.getByNameContainingIgnoreCase(name, pageable);
        return userPage.map(UserMapper::toResponse);
    }


    @Override
    public Page<UserResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepo.getAll(pageRequest);
        return userPage
                .map(UserMapper::toResponse);
    }

    @Override
    public MessageResponse update(String id, UserSignUpRequest request) {
        User user = userRepo.getById(id);
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setImage(request.getImage().trim());
        user.setLocation(request.getLocation().trim());

        userRepo.save(user);
        return new MessageResponse("User updated successfully.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        User user = userRepo.getById(id);
        user.setStatus(Status.DELETED);
        userRepo.save(user);
        // delete all review
        this.reviewService.softDeleteById(user.getId());
        return new MessageResponse("User deleted successfully.");
    }


    @Override
    public MessageResponse hardDeleteById(String id) {
        User user = userRepo.getById(id);
        userRepo.delete(user);
        return new MessageResponse("User deleted successfully.");
    }
}