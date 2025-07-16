package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.Category;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryMongoRepo extends MongoRepository<Category,String> {

    Optional<Category> findByIdAndStatus(String id, Status status);

    Optional<Category> findByNameAndStatus(String name, Status status);

    List<Category> findAllByStatus(Status status);

    Page<Category> findAllByStatus(Status status, Pageable pageable);
}
