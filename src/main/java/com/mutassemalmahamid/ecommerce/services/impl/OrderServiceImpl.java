package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.OrderMapper;
import com.mutassemalmahamid.ecommerce.model.document.Order;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.repository.OrderRepo;
import com.mutassemalmahamid.ecommerce.services.OrderService;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    public OrderServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public OrderResponse create(String userId, OrderRequest request) {
        Order order = OrderMapper.toEntity(request, userId);
        Order saved = orderRepo.save(order);
        return OrderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getById(String id) {
        Order order = orderRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllByUserId(String userId) {
        return orderRepo.getAllByUserId(userId).stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> getAll(Pageable pageable) {
        return orderRepo.getAll(pageable).map(OrderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> getAllByUserId(String userId, Pageable pageable) {
        return orderRepo.getAllByUserId(userId, pageable).map(OrderMapper::toResponse);
    }

    @Override
    public OrderResponse update(String id, OrderRequest request) {
        Order order = orderRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        OrderMapper.updateEntity(order, request);
        order.setUpdatedAt(LocalDateTime.now());
        Order updated = orderRepo.save(order);
        return OrderMapper.toResponse(updated);
    }

    @Override
    public MessageResponse updateStatus(String id, OrderStatus status) {
        Order order = orderRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);
        return AssistantHelper.toMessageResponse("Order status updated.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        Order order = orderRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        order.setDeletedAt(LocalDateTime.now());
        orderRepo.save(order);
        return AssistantHelper.toMessageResponse("Order soft deleted.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        orderRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Order hard deleted.");
    }
}

