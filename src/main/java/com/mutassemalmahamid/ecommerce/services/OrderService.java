package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponse create(String userId, OrderRequest request);
    OrderResponse getById(String id);
    List<OrderResponse> getAllByUserId(String userId);
    Page<OrderResponse> getAll(Pageable pageable);
    Page<OrderResponse> getAllByUserId(String userId, Pageable pageable);
    OrderResponse update(String id, OrderRequest request);
    MessageResponse updateStatus(String id, OrderStatus status);
    MessageResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
}

