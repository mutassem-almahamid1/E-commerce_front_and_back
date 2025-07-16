package com.mutassemalmahamid.ecommerce.model.document;

import com.mutassemalmahamid.ecommerce.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String brand;
    private String categoryId;
    private int stockQuantity;

    private List<String> images;
    private Map<String, String> technicalSpecs;

    private double avgRating;
    private int reviewCount;

    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
