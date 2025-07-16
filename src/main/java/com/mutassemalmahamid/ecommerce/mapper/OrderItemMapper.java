package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.OrderItem;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderItemRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderItemResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

    public static OrderItem toEntity(OrderItemRequest request) {
        return OrderItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
    }

    public static OrderItemResponse toResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    public static List<OrderItem> toEntityList(List<OrderItemRequest> requests) {
        return requests.stream().map(OrderItemMapper::toEntity).collect(Collectors.toList());
    }

    public static List<OrderItemResponse> toResponseList(List<OrderItem> items) {
        return items.stream().map(OrderItemMapper::toResponse).collect(Collectors.toList());
    }
}

