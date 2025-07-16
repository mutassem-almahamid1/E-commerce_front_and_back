package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.Category;
import com.mutassemalmahamid.ecommerce.model.dto.request.CategoryReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.CategoryResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;

import java.time.LocalDateTime;

public class CategoryMapper {

    public static Category toEntity(CategoryReq categoryReq) {
        if (categoryReq == null) return null;
        return Category.builder()
                .name(categoryReq.getCategoryName())
                .description(categoryReq.getCategoryDescription())
                .image(categoryReq.getCategoryImage())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .categoryName(category.getName())
                .categoryDescription(category.getDescription())
                .categoryImage(category.getImage())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .deletedAt(category.getDeletedAt())
                .build();
    }

    public static void updateEntity(Category category, CategoryReq categoryReq) {
            category.setName(categoryReq.getCategoryName());
            category.setDescription(categoryReq.getCategoryDescription());
            category.setImage(categoryReq.getCategoryImage());
            category.setUpdatedAt(LocalDateTime.now());
    }
}
