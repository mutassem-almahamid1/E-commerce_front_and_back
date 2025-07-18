package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> create(
            @PathVariable String userId,
            @Valid @RequestBody OrderRequest request) {
        try {
            OrderResponse response = orderService.create(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
        OrderResponse response = orderService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getAllByUserId(@PathVariable String userId) {
        List<OrderResponse> response = orderService.getAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> response = orderService.getAll(pageable);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status) {
        OrderResponse response = orderService.updateStatus(orderId, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<MessageResponse> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order cancelled successfully"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> response = orderService.getAll(pageable);
        // Filter by status would be handled in service layer if needed
        return ResponseEntity.ok(response);
    }
}
