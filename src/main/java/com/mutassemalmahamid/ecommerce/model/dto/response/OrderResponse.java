package com.mutassemalmahamid.ecommerce.model.dto.response;

import com.mutassemalmahamid.ecommerce.model.document.Address;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;
    private String userId;
    private String orderNumber;

    private Address shippingAddress;
    private Address billingAddress;

    private double totalAmount;
    private double shippingCost;
    private double taxAmount;
    private double discountAmount;
    private double finalAmount;

    private List<OrderItemResponse> items;

    private PaymentMethod paymentMethod;
    private String paymentId;
    private boolean isPaid;

    private OrderStatus status;
    private String notes;
    private String trackingNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    private int totalItems;
    private String statusDescription;
}
