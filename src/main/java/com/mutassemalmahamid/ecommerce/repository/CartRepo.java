package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepo {
    Cart save(Cart cart);

    List<Cart> saveAll(List<Cart> carts);

    Optional<Cart> getByIdIfPresent(String id);

    Optional<Cart> getByUserIdIfPresent(String userId);

    List<Cart> getAll();

    void deleteById(String cartId);

    void delete(Cart cart);
}
