package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Brand;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandRepo {
    Brand save(Brand brand);

    List<Brand> saveAll(List<Brand> brands);

    Optional<Brand> getByIdIfPresent(String id);

    Optional<Brand> getByNameIfPresent(String name);

    Page<Brand> getAllByStatus(Status status, Pageable pageable);

    Page<Brand> getAll(Pageable pageable);

    Page<Brand> getAllActive(Pageable pageable);

    List<Brand> getAll();

    void deleteById(String brandId);

    void delete(Brand brand);
}
