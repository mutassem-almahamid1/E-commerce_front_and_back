package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.document.Brand;
import com.mutassemalmahamid.ecommerce.model.dto.request.BrandRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.BrandResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;

import java.time.LocalDateTime;

public class BrandMapper {

    public static Brand toEntity(BrandRequest brandRequest) {
        return Brand.builder()
                .name(AssistantHelper.trimString(brandRequest.getName()))
                .description(AssistantHelper.trimString(brandRequest.getDescription()))
                .logoUrl(AssistantHelper.trimString(brandRequest.getLogoUrl()))
                .websiteUrl(AssistantHelper.trimString(brandRequest.getWebsiteUrl()))
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BrandResponse toResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .websiteUrl(brand.getWebsiteUrl())
                .status(brand.getStatus().name())
                .createdAt(brand.getCreatedAt())
                .build();
    }

    public static void updateEntity(Brand brand, BrandRequest brandRequest) {
            brand.setName(AssistantHelper.trimString(brandRequest.getName()));
            brand.setDescription(AssistantHelper.trimString(brandRequest.getDescription()));
            brand.setLogoUrl(AssistantHelper.trimString(brandRequest.getLogoUrl()));
            brand.setWebsiteUrl(AssistantHelper.trimString(brandRequest.getWebsiteUrl()));
            brand.setStatus(brandRequest.getStatus());
            brand.setUpdatedAt(LocalDateTime.now());
    }
}
