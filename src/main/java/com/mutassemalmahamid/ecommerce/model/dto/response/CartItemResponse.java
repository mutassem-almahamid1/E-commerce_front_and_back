package com.mutassemalmahamid.ecommerce.model.dto.response;

import com.mutassemalmahamid.ecommerce.model.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String productId;
    private Integer quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
