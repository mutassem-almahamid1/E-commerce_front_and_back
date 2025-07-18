package com.mutassemalmahamid.ecommerce.model.dto.response;

import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private double price;
    private double totalPrice;
    private OrderStatus status;
    private String productDescription;
    private String brandName;
    private String categoryName;
}
