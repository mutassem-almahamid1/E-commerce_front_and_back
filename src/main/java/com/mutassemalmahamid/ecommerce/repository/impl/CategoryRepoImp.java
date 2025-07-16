package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.model.document.Category;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.CategoryRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.CategoryMongoRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepoImp implements CategoryRepo {
    private final CategoryMongoRepo categoryMongoRepo;

    public CategoryRepoImp(CategoryMongoRepo categoryMongoRepo) {
        this.categoryMongoRepo = categoryMongoRepo;
    }

    @Override
    public Category save(Category category) {
        return categoryMongoRepo.save(category);
    }

    @Override
    public List<Category> saveAll(List<Category> category) {
        return categoryMongoRepo.saveAll(category);
    }

    @Override
    public Optional<Category> getByNameIfPresent(String name) {
        return categoryMongoRepo.findByNameAndStatus(name, Status.ACTIVE);
    }

    @Override
    public Optional<Category> getByIdIfPresent(String id) {
        return categoryMongoRepo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Optional<Category> getByIdIgnoreStatus(String id) {
        return categoryMongoRepo.findById(id);
    }

    @Override
    public Page<Category> getAllActive(Pageable pageable) {
        return categoryMongoRepo.findAllByStatus(Status.ACTIVE, pageable);
    }

    @Override
    public Page<Category> getAll(Pageable pageable) {
        return categoryMongoRepo.findAll(pageable);
    }

    @Override
    public List<Category> getAll() {
        return categoryMongoRepo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public List<Category> getAllIgnoreStatus() {
        return categoryMongoRepo.findAll();
    }

    @Override
    public void deleteById(String id) {
        categoryMongoRepo.deleteById(id);
    }

    @Override
    public void delete(Category category) {
        categoryMongoRepo.delete(category);
    }
}
