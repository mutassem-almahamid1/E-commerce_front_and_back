package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import com.mutassemalmahamid.ecommerce.model.document.CartItem;

public interface CartService {
    Cart getCartByUserId(String userId);
    Cart addOrUpdateItem(String userId, CartItem item);
    Cart removeItem(String userId, String productId);
    void clearCart(String userId);
}

