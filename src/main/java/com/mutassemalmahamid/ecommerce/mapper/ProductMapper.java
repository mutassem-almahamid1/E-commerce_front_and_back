package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.document.Product;
import com.mutassemalmahamid.ecommerce.model.dto.request.ProductReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ProductResponse;
import com.mutassemalmahamid.ecommerce.model.enums.ItemStatus;
import com.mutassemalmahamid.ecommerce.model.enums.Status;

import java.time.LocalDateTime;

public class ProductMapper {

    public static Product toEntity(ProductReq productReq) {
        return Product.builder()
                .name(AssistantHelper.trimString(productReq.getName()))
                .description(AssistantHelper.trimString(productReq.getDescription()))
                .brand(AssistantHelper.trimString(productReq.getBrand()))
                .price(productReq.getPrice())
                .categoryId(AssistantHelper.trimString(productReq.getCategoryId()))
                .stockQuantity(productReq.getQuantity())
                .images(AssistantHelper.trimListValues(productReq.getImages()))
                .technicalSpecs(AssistantHelper.trimMapStringValues(productReq.getTechnicalSpecs()))
                .avgRating(0.0)
                .reviewCount(0)
                .status(Status.ACTIVE)
                .itemStatus(ItemStatus.IN_STOCK)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .quantity(product.getStockQuantity())
                .images(product.getImages())
                .technicalSpecs(product.getTechnicalSpecs())
                .avgRating(product.getAvgRating())
                .reviewCount(product.getReviewCount())
                .price(product.getPrice())
                .categoryId(product.getCategoryId())
                .status(product.getStatus())
                .itemStatus(product.getItemStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .deletedAt(product.getDeletedAt())
                .build();
    }

    public static ProductResponse toResponse(Product product, double avgRating, int reviewCount) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .quantity(product.getStockQuantity())
                .images(product.getImages())
                .technicalSpecs(product.getTechnicalSpecs())
                .avgRating(avgRating)
                .reviewCount(reviewCount)
                .price(product.getPrice())
                .categoryId(product.getCategoryId())
                .status(product.getStatus())
                .itemStatus(product.getItemStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .deletedAt(product.getDeletedAt())
                .build();
    }

    public static void updateEntity(Product product, ProductReq productReq) {
            product.setName(AssistantHelper.trimString(productReq.getName()));
            product.setDescription(AssistantHelper.trimString(productReq.getDescription()));
            product.setBrand(AssistantHelper.trimString(productReq.getBrand()));
            product.setStockQuantity(productReq.getQuantity());
            product.setImages(AssistantHelper.trimListValues(productReq.getImages()));
            product.setTechnicalSpecs(AssistantHelper.trimMapStringValues(productReq.getTechnicalSpecs()));
            product.setPrice(productReq.getPrice());
            product.setCategoryId(AssistantHelper.trimString(productReq.getCategoryId()));
            product.setUpdatedAt(LocalDateTime.now());
    }
}
