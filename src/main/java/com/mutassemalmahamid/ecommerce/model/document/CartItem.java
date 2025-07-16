package com.mutassemalmahamid.ecommerce.model.document;


import com.mutassemalmahamid.ecommerce.model.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private String productId;

    private Integer quantity;

    private double unitPrice;

    private double totalPrice;

    private ItemStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}