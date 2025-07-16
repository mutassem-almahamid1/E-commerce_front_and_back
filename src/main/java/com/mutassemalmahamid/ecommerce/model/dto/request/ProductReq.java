package com.mutassemalmahamid.ecommerce.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReq {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private double price;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be positive")
    private int quantity;

    @NotEmpty(message = "At least one image is required")
    private List<String> images;

    @NotEmpty(message = "Technical specifications are required")
    private Map<String, String> technicalSpecs;
}
