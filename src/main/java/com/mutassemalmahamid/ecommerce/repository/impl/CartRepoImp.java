package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.model.document.Cart;
import com.mutassemalmahamid.ecommerce.repository.CartRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.CartMongoRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartRepoImp implements CartRepo {

    private final CartMongoRepo cartMongoRepo;

    public CartRepoImp(CartMongoRepo cartMongoRepo) {
        this.cartMongoRepo = cartMongoRepo;
    }

    @Override
    public Cart save(Cart cart) {
        return cartMongoRepo.save(cart);
    }

    @Override
    public List<Cart> saveAll(List<Cart> carts) {
        return cartMongoRepo.saveAll(carts);
    }

    @Override
    public Optional<Cart> getByIdIfPresent(String id) {
        return cartMongoRepo.findById(id);
    }

    @Override
    public Optional<Cart> getByUserIdIfPresent(String userId) {
        return cartMongoRepo.findByUserId(userId);
    }

    @Override
    public List<Cart> getAll() {
        return cartMongoRepo.findAll();
    }

    @Override
    public void deleteById(String cartId) {
        cartMongoRepo.deleteById(cartId);
    }

    @Override
    public void delete(Cart cart) {
        cartMongoRepo.delete(cart);
    }
}
