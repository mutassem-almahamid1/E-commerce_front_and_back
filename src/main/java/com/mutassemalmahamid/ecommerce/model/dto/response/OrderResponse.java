package com.mutassemalmahamid.ecommerce.model.dto.response;

import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String userId;
    private String orderNumber;

    private AddressResponse shippingAddress;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private List<OrderItemResponse> items;

    private OrderStatus status;

    private String createdBy;
    private String updatedBy;
    private String deletedBy;
}

