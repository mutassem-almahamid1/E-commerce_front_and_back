package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Category;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo {
    Category save(Category category);

    List<Category> saveAll(List<Category> category);

    Optional<Category> getByNameIfPresent(String name);

    Optional<Category> getByIdIfPresent(String id);

    Optional<Category> getByIdIgnoreStatus(String id);


    Page<Category> getAllActive(Pageable pageable);

    Page<Category> getAll(Pageable pageable);

    List<Category> getAll();

    List<Category> getAllIgnoreStatus();

    void deleteById(String id);

    void delete(Category category);
}
