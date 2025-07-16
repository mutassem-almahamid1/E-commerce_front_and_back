package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.Order;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderMongoRepo extends MongoRepository<Order, String> {

    Optional<Order> findById(String id);

    List<Order> findAllByUserId(String userId);

    Page<Order> findAllByUserId(String userId, Pageable pageable);

    Optional<Order> findByIdAndStatus(String id, Status status);

    List<Order> findAllByUserIdAndStatus(String userId, Status status);

    List<Order> findAllByStatus(Status status);

    Page<Order> findAllByStatus(Status status, Pageable pageable);
    Page<Order> findAllByUserIdAndStatus(String userId, Status status, Pageable pageable);
}
