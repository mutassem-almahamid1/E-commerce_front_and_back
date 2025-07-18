package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.User;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

    User save(User user);

    List<User> saveAll(List<User> users);

    Optional<User> getByIdIfPresent(String id);

    Optional<User> getByEmailIfPresent(String email);

    Optional<User> getByEmailIgnoreStatus(String email);

    Optional<User> findByEmailOrUsername(String email, String username);

    Page<User> getAll(Pageable pageable);

    Page<User> getAllActive(Pageable pageable);

    void deleteById(String id);

    void delete(User user);

    Optional<User> getByUsernameIfPresent(String username);

    User getByIdAndStatusNot(String id, Status status);

    User getByUsername(String username);

    Page<User> getByNameContainingIgnoreCase(String name, Pageable pageable);

    User getById(String id);

    User getByEmail(String email);

    User getByRefreashToken(String refreshToken);
}
