package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.model.document.User;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.UserRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.UserMongRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepoImp implements UserRepo {

    private final UserMongRepo userMongRepo;

    public UserRepoImp(UserMongRepo userMongRepo) {
        this.userMongRepo = userMongRepo;
    }

    @Override
    public User save(User user){
        return userMongRepo.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users){
        return userMongRepo.saveAll(users);
    }

    @Override
    public Optional<User> getByIdIfPresent(String id){
        return userMongRepo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Optional<User> getByEmailIfPresent(String email) {
        return userMongRepo.findByEmailAndStatus(email, Status.ACTIVE);
    }

    @Override
    public Optional<User> getByEmailIgnoreStatus(String email) {
        return userMongRepo.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailOrUsername(String email, String username) {
        return userMongRepo.findByEmailOrUsername(email, username);
    }

    @Override
    public Page<User> getAllActive(Pageable pageable) {
        return userMongRepo.findAllByStatus(Status.ACTIVE, pageable);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userMongRepo.findAll(pageable);
    }

    @Override
    public void deleteById(String id) {
        userMongRepo.deleteById(id);
    }

    @Override
    public void delete(User user) {
        userMongRepo.delete(user);
    }

    @Override
    public Optional<User> getByUsernameIfPresent(String username) {
        return userMongRepo.findByUsername(username);
    }

    @Override
    public User getByIdAndStatusNot(String id, Status status) {
        return userMongRepo.findByIdAndStatusNot(id, status)
                .orElseThrow(() -> new NotFoundException("User not found or is deleted with id: " + id));
    }

    @Override
    public User getByUsername(String username) {
        return userMongRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }

    @Override
    public Page<User> getByNameContainingIgnoreCase(String name, Pageable pageable) {
        return userMongRepo.findByUsernameContainingIgnoreCase(name, pageable);
    }

    @Override
    public User getById(String id) {
        return userMongRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public User getByEmail(String email) {
        return userMongRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public User getByRefreashToken(String refreshToken) {
        return userMongRepo.findByRefreshTokenAndStatus(refreshToken, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
