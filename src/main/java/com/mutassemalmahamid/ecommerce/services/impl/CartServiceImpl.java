package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import com.mutassemalmahamid.ecommerce.model.document.CartItem;
import com.mutassemalmahamid.ecommerce.repository.CartRepository;
import com.mutassemalmahamid.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCartByUserId(String userId) {
        return cartRepository.findFirstByUserIdOrderByCreatedAtDesc(userId).orElseGet(() -> {
            Cart cart = Cart.builder().userId(userId).cartItems(new ArrayList<>()).build();
            return cartRepository.save(cart);
        });
    }

    @Override
    public Cart addOrUpdateItem(String userId, CartItem item) {
        Cart cart = getCartByUserId(userId);
        boolean found = false;
        for (CartItem ci : cart.getCartItems()) {
            if (ci.getProductId().equals(item.getProductId())) {
                ci.setQuantity(ci.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            cart.getCartItems().add(item);
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItem(String userId, String productId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().removeIf(ci -> ci.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
