package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.Order;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Collections;

public class OrderMapper {

    public static Order toEntity(OrderRequest orderRequest, String userId) {
        return Order.builder()
                .userId(userId)
                .shippingAddress(AddressMapper.toEntity(orderRequest.getShippingAddress()))
                .items(OrderItemMapper.toEntityList(orderRequest.getItems()))
                .paymentMethod(orderRequest.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderDate(LocalDateTime.now())
                .build();
    }

    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getId())
                .shippingAddress(AddressMapper.toResponse(order.getShippingAddress()))
                .totalAmount(order.getTotalAmount())
                .items(OrderItemMapper.toResponseList(order.getItems()))
                .status(order.getStatus())
                .build();
    }

    public static void updateEntity(Order order, OrderRequest orderRequest) {
        order.setShippingAddress(AddressMapper.toEntity(orderRequest.getShippingAddress()));
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setItems(OrderItemMapper.toEntityList(orderRequest.getItems()));
        order.setStatus(order.getStatus());
        order.setUpdatedAt(LocalDateTime.now());
    }
}

