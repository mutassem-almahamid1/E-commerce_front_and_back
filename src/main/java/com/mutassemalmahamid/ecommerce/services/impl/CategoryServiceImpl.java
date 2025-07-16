package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.CategoryMapper;
import com.mutassemalmahamid.ecommerce.model.document.Category;
import com.mutassemalmahamid.ecommerce.model.dto.request.CategoryReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.CategoryResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.repository.CategoryRepo;
import com.mutassemalmahamid.ecommerce.services.CategoryService;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public CategoryResponse create(CategoryReq request) {
        Category category = CategoryMapper.toEntity(request);
        Category saved = categoryRepo.save(category);
        return CategoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse getById(String id) {
        Category category = categoryRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse update(String id, CategoryReq request) {
        Category category = categoryRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        CategoryMapper.updateEntity(category, request);
        category.setUpdatedAt(LocalDateTime.now());
        Category updated = categoryRepo.save(category);
        return CategoryMapper.toResponse(updated);
    }

    @Override
    public MessageResponse updateStatus(String id, Status status) {
        Category category = categoryRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setStatus(status);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepo.save(category);
        return AssistantHelper.toMessageResponse("Status updated successfully.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        Category category = categoryRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setStatus(Status.DELETED);
        category.setDeletedAt(LocalDateTime.now());
        categoryRepo.save(category);
        return AssistantHelper.toMessageResponse("Category soft deleted.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        categoryRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Category hard deleted.");
    }

    @Override
    public Page<CategoryResponse> getAll(Pageable pageable) {
        return categoryRepo.getAll(pageable).map(CategoryMapper::toResponse);
    }

    @Override
    public List<CategoryResponse> getAllActive() {
        return categoryRepo.getAll().stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}

