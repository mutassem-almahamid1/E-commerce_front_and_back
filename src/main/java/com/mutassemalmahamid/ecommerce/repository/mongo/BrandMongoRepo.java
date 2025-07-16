package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.Brand;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandMongoRepo extends MongoRepository<Brand, String> {
    Optional<Brand> findByNameAndStatus(String name, Status status);
    Optional<Brand> findByIdAndStatus(String id, Status status);

    List<Brand> findAllByStatus(Status status);
    Page<Brand> findAllByStatus(Status status, Pageable pageable);

}
