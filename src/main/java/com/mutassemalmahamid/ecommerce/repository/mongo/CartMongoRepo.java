package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartMongoRepo extends MongoRepository<Cart, String> {
    // Get the most recent cart for a user (in case of multiple carts)
    Optional<Cart> findFirstByUserIdOrderByCreatedAtDesc(String userId);

    // Get all carts for a user (for debugging/cleanup)
    List<Cart> findAllByUserId(String userId);
}
