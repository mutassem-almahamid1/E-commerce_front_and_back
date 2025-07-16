package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.Product;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductMongoRepo extends MongoRepository<Product,String> {

    Optional<Product> findByNameAndStatus(String name, Status status);
    Optional<Product> findByIdAndStatus(String id, Status status);

    // تحسين: استخدم Pageable بدلاً من PageRequest
    Page<Product> findAllByStatus(Status status, Pageable pageable);
    Page<Product> findAllByCategoryIdAndStatus(String categoryId, Status status, Pageable pageable);
    Page<Product> findAllByBrandAndStatus(String brand, Status status, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndStatus(String name, Status status, Pageable pageable);
    List<Product> findTop10ByOrderByCreatedAtDesc();
    List<Product> findTop10ByOrderByAvgRatingDesc();
}
