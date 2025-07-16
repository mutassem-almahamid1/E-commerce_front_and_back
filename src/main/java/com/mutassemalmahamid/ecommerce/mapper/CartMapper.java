package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import com.mutassemalmahamid.ecommerce.model.document.CartItem;
import com.mutassemalmahamid.ecommerce.model.dto.response.CartResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponse toResponse(Cart cart) {
        List<CartItem> items = cart.getCartItems() != null ? cart.getCartItems() : List.of();
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .cartItems(items.stream().map(CartItemMapper::toResponse).collect(Collectors.toList()))
                .totalAmount(cart.getTotalAmount())
                .itemCount(items.size())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .deletedAt(cart.getDeletedAt())
                .build();
    }

    public static Cart toEntity(String userId, List<CartItem> cartItems) {
        return Cart.builder()
                .userId(userId)
                .cartItems(cartItems)
                .totalAmount(calculateTotalAmount(cartItems))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static void updateEntity(Cart cart, List<CartItem> cartItems) {
        cart.setCartItems(cartItems);
        cart.setTotalAmount(calculateTotalAmount(cartItems));
        cart.setUpdatedAt(LocalDateTime.now());
    }

    private static double calculateTotalAmount(List<CartItem> cartItems) {
        if (cartItems == null) return 0.0;
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

}
