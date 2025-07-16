package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.CartRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.CartResponse;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;

import java.util.List;

public interface CartService {
    CartResponse create(String userId, CartRequest request);
    CartResponse getById(String id);
    CartResponse getByUserId(String userId);
    CartResponse update(String userId, CartRequest request);
    MessageResponse deleteById(String id);
    MessageResponse deleteByUserId(String userId);
    List<CartResponse> getAll();
}

