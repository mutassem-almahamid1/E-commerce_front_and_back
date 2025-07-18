package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import com.mutassemalmahamid.ecommerce.model.document.CartItem;
import com.mutassemalmahamid.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/add")
    public Cart addOrUpdateItem(@PathVariable String userId, @RequestBody CartItem item) {
        return cartService.addOrUpdateItem(userId, item);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public Cart removeItem(@PathVariable String userId, @PathVariable String productId) {
        return cartService.removeItem(userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}

