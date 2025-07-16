package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.mapper.UserMapper;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.model.document.User;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReviewService reviewService;


    @Override
    public UserResponse signUp(UserSignUpRequest request) {
        User toDocument = UserMapper.toEntity(request);

        if (userRepo.getByUsernameIfPresent(toDocument.getUsername()).isPresent()) {
          //  throw new ConflictException("Username is already taken");
        }


        if (userRepo.getByEmailIfPresent(toDocument.getEmail()).isPresent()) {
            //throw new ConflictException("Email is already in use");
        }

        //toDocument.setPassword(passwordEncoder.encode(toDocument.getPassword()));
        User user = this.userRepo.save(toDocument);
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getById(String id) {
        Optional<User> user = userRepo.getByIdIfPresent(id);
        return UserMapper.toResponse(user);
    }

    @Override
    public MessageResponse updateStatus(String id, Status status) {
        User user = userRepo.getByIdAndStatusNotDeleted(id);
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        this.userRepo.save(user);
        return AssistantHelper.toMessageResponse("Updated Successfully.");
    }

    @Override
    public UserResponse getByUsername(String username) {
        User user = userRepo.getByUsername(username);
        return UserMapper.toResponse(Optional.ofNullable(user));
    }

    @Override
    public Page<UserResponse> getAllByName(String name) {
        Page<User> users = this.userRepo.getByNameContainingIgnoreCase(name);
        return users
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
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
        this.reviewService.softDeleteByUserId(user.getId());
        return new MessageResponse("User deleted successfully.");
    }


    @Override
    public MessageResponse hardDeleteById(String id) {
        User user = userRepo.getById(id);
        userRepo.delete(user);
        return new MessageResponse("User deleted successfully.");
    }
    }