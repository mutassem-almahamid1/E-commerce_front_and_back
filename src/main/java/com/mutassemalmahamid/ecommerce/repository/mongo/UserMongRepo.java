package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.User;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMongRepo extends MongoRepository<User,String> {

    Optional<User> findByIdAndStatus(String id, Status status);

    Optional<User> findByIdAndStatusNot(String id, Status status);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByEmailAndStatusNot(String email, Status status);

    Optional<User> findByUsernameAndStatus(String username, Status status);

    Optional<User> findByEmailOrUsername(String email, String username);

    List<User> findAllByStatusNot(Status status);

    Page<User> findAllByStatusNot(Status status, Pageable pageable);

    Page<User> findAllByStatus(Status status, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCase(String name, Pageable pageable);

    Optional<User> findByRefreshTokenAndStatus(String refreshToken, Status status);
}
