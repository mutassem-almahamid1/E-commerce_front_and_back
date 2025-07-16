package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.CartItemMapper;
import com.mutassemalmahamid.ecommerce.mapper.CartMapper;
import com.mutassemalmahamid.ecommerce.model.document.Cart;
import com.mutassemalmahamid.ecommerce.model.dto.request.CartItemRequest;
import com.mutassemalmahamid.ecommerce.model.dto.request.CartRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.CartResponse;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.repository.CartRepo;
import com.mutassemalmahamid.ecommerce.services.CartService;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;

    public CartServiceImpl(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    private List<Double> fetchUnitPrices(List<CartItemRequest> cartItemRequests) {
        return cartItemRequests.stream().map(req -> 1.0).collect(Collectors.toList());
    }

    @Override
    public CartResponse create(String userId, CartRequest request) {
        List<Double> unitPrices = fetchUnitPrices(request.getCartItems());
        Cart cart = CartMapper.toEntity(userId, CartItemMapper.toEntityList(request.getCartItems(), unitPrices));
        Cart saved = cartRepo.save(cart);
        return CartMapper.toResponse(saved);
    }

    @Override
    public CartResponse getById(String id) {
        Cart cart = cartRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        return CartMapper.toResponse(cart);
    }

    @Override
    public CartResponse getByUserId(String userId) {
        Cart cart = cartRepo.getByUserIdIfPresent(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        return CartMapper.toResponse(cart);
    }

    @Override
    public CartResponse update(String userId, CartRequest request) {
        Cart cart = cartRepo.getByUserIdIfPresent(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        List<Double> unitPrices = fetchUnitPrices(request.getCartItems());
        CartMapper.updateEntity(cart, CartItemMapper.toEntityList(request.getCartItems(), unitPrices));
        Cart updated = cartRepo.save(cart);
        return CartMapper.toResponse(updated);
    }

    @Override
    public MessageResponse deleteById(String id) {
        cartRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Cart deleted.");
    }

    @Override
    public MessageResponse deleteByUserId(String userId) {
        Cart cart = cartRepo.getByUserIdIfPresent(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        cartRepo.delete(cart);
        return AssistantHelper.toMessageResponse("Cart deleted.");
    }

    @Override
    public List<CartResponse> getAll() {
        return cartRepo.getAll().stream()
                .map(CartMapper::toResponse)
                .collect(Collectors.toList());
    }
}
