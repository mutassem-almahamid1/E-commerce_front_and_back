package com.mutassemalmahamid.ecommerce.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReq {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotNull(message = "Rating is required")
    @Min(message = "Rating must be between 1 and 5", value = 1)
    @Max(message = "Rating must be between 1 and 5", value = 5)
    private int rating;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;
}
