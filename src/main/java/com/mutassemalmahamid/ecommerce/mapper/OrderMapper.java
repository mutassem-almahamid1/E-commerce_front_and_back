package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.Order;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.repository.ProductRepo;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequest request, String userId) {
        return Order.builder()
                .userId(userId)
                .orderNumber(generateOrderNumber())
                .shippingAddress(request.getShippingAddress()) // مباشرة لأن Address هو Document class
                .billingAddress(request.getBillingAddress() != null ?
                    request.getBillingAddress() : request.getShippingAddress())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .isPaid(false)
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static OrderResponse toResponse(Order order, ProductRepo productRepo) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderNumber(order.getOrderNumber())
                .shippingAddress(order.getShippingAddress()) // مباشرة
                .billingAddress(order.getBillingAddress())
                .totalAmount(order.getTotalAmount())
                .shippingCost(order.getShippingCost())
                .taxAmount(order.getTaxAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .items(order.getItems().stream()
                        .map(OrderItemMapper::toResponse)
                        .collect(Collectors.toList()))
                .paymentMethod(order.getPaymentMethod())
                .paymentId(order.getPaymentId())
                .isPaid(order.isPaid())
                .status(order.getStatus())
                .notes(order.getNotes())
                .trackingNumber(order.getTrackingNumber())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .totalItems(order.getItems().stream()
                        .mapToInt(item -> item.getQuantity())
                        .sum())
                .statusDescription(getStatusDescription(order.getStatus()))
                .build();
    }

    private static String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" +
               UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private static String getStatusDescription(OrderStatus status) {
        switch (status) {
            case PENDING:
                return "Order is being processed";
            case CONFIRMED:
                return "Order has been confirmed";
            case PROCESSING:
                return "Order is being prepared";
            case SHIPPED:
                return "Order has been shipped";
            case DELIVERED:
                return "Order has been delivered";
            case CANCELLED:
                return "Order has been cancelled";
            case RETURNED:
                return "Order has been returned";
            default:
                return "Unknown status";
        }
    }
}
