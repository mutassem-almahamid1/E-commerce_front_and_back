package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.model.dto.request.CartRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.CartResponse;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartResponse> create(@Valid @PathVariable String userId, @RequestBody CartRequest request) {
        CartResponse response = cartService.create(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getById(@PathVariable String id) {
        CartResponse response = cartService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getByUserId(@PathVariable String userId) {
        CartResponse response = cartService.getByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<CartResponse> update(@PathVariable String userId, @Valid@RequestBody CartRequest request) {
        CartResponse response = cartService.update(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteById(@PathVariable String id) {
        MessageResponse response = cartService.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<MessageResponse> deleteByUserId(@PathVariable String userId) {
        MessageResponse response = cartService.deleteByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAll() {
        List<CartResponse> response = cartService.getAll();
        return ResponseEntity.ok(response);
    }
}

