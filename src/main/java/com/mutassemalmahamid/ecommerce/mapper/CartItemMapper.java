package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.CartItem;
import com.mutassemalmahamid.ecommerce.model.dto.request.CartItemRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.CartItemResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CartItemMapper {

    public static CartItem toEntity(CartItemRequest cartItemRequest, double unitPrice) {
        return CartItem.builder()
                .productId(cartItemRequest.getProductId())
                .quantity(cartItemRequest.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(unitPrice * cartItemRequest.getQuantity())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static List<CartItem> toEntityList(List<CartItemRequest> cartItemRequests, List<Double> unitPrices) {
        List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < cartItemRequests.size(); i++) {
            CartItemRequest request = cartItemRequests.get(i);
            double unitPrice = unitPrices.get(i);
            cartItems.add(toEntity(request, unitPrice));
        }

        return cartItems;
    }

    public static CartItemResponse toResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .deletedAt(cartItem.getDeletedAt())
                .build();
    }


    public static void updateEntity(CartItem cartItem, CartItemRequest cartItemRequest, double unitPrice) {
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setUnitPrice(unitPrice);
        cartItem.setTotalPrice(unitPrice * cartItem.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());
    }
}
