package com.mutassemalmahamid.ecommerce.model.dto.response;

import com.mutassemalmahamid.ecommerce.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private String brand;
    private String categoryId;
    private String categoryName;
    private int quantity;

    private List<String> images;
    private Map<String, String> technicalSpecs;

    private double avgRating;
    private int reviewCount;

    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
