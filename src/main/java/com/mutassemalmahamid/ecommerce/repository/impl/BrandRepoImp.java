package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.model.document.Brand;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.BrandRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.BrandMongoRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BrandRepoImp implements BrandRepo {

    private final BrandMongoRepo brandMongoRepo;

    public BrandRepoImp(BrandMongoRepo brandMongoRepo) {
        this.brandMongoRepo = brandMongoRepo;
    }

    @Override
    public Brand save(Brand brand) {
        return brandMongoRepo.save(brand);
    }

    @Override
    public List<Brand> saveAll(List<Brand> brands) {
        return brandMongoRepo.saveAll(brands);
    }

    @Override
    public Optional<Brand> getByIdIfPresent(String id) {
        return brandMongoRepo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Optional<Brand> getByNameIfPresent(String name) {
        return brandMongoRepo.findByNameAndStatus(name, Status.ACTIVE);
    }

    @Override
    public Page<Brand> getAllByStatus(Status status, Pageable pageable) {
        return brandMongoRepo.findAllByStatus(status, pageable);
    }

    @Override
    public Page<Brand> getAll(Pageable pageable) {
        return brandMongoRepo.findAll( pageable);
    }

    @Override
    public Page<Brand> getAllActive(Pageable pageable) {
        return brandMongoRepo.findAllByStatus(Status.ACTIVE, pageable);
    }

    @Override
    public List<Brand> getAll() {
        return brandMongoRepo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public void deleteById(String brandId) {
        brandMongoRepo.deleteById(brandId);
    }

    @Override
    public void delete(Brand brand) {
        brandMongoRepo.delete(brand);
    }
}
