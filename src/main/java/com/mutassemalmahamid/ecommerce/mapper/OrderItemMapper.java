package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.OrderItem;
import com.mutassemalmahamid.ecommerce.model.document.Product;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderItemRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderItemResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.repository.ProductRepo;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

    public static OrderItem toEntity(OrderItemRequest request, ProductRepo productRepo) {
        Product product = productRepo.getByIdIfPresent(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        return OrderItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .price(product.getPrice())
                .totalPrice(product.getPrice() * request.getQuantity())
                .status(OrderStatus.PENDING)
                .productName(product.getName())
                .productImageUrl(product.getImages().get(0))
                .build();
    }

    public static List<OrderItem> toEntityList(List<OrderItemRequest> requests, ProductRepo productRepo) {
        return requests.stream()
                .map(request -> toEntity(request, productRepo))
                .collect(Collectors.toList());
    }

    public static OrderItemResponse toResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .imageUrl(orderItem.getProductImageUrl())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .totalPrice(orderItem.getTotalPrice())
                .status(orderItem.getStatus())
                .build();
    }

    public static List<OrderItemResponse> toResponseList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemMapper::toResponse)
                .collect(Collectors.toList());
    }
}
