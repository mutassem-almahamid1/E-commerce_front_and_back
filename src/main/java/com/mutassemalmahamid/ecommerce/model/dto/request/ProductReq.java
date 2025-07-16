package com.mutassemalmahamid.ecommerce.model.dto.request;


import com.mutassemalmahamid.ecommerce.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReq {
    @NotBlank(message = "Product ")
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private double price;
    @NotBlank
    private String brand;
    @NotBlank
    private String categoryId;
    @NotBlank
    private int quantity;
    @NotBlank
    private List<String> images;
    @NotBlank
    private Map<String, String> technicalSpecs;
}
