package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    // Use the safer method that gets the most recent cart to avoid non-unique result errors
    Optional<Cart> findFirstByUserIdOrderByCreatedAtDesc(String userId);

    // Get all carts for a user (for cleanup purposes)
    List<Cart> findAllByUserId(String userId);
}
