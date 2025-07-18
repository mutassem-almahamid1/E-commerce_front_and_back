package com.mutassemalmahamid.ecommerce.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String productId;
    private int quantity;
    private double price;

    public double getTotalPrice() {
        return price * quantity;
    }
}
