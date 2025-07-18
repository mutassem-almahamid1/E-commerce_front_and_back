package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.model.dto.request.CategoryReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.CategoryResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryReq request) {
        CategoryResponse response = categoryService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable String id) {
        CategoryResponse response = categoryService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@Valid @PathVariable String id, @RequestBody CategoryReq request) {
        CategoryResponse response = categoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable String id, @RequestParam Status status) {
        MessageResponse response = categoryService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> softDelete(@PathVariable String id) {
        MessageResponse response = categoryService.softDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<MessageResponse> hardDelete(@PathVariable String id) {
        MessageResponse response = categoryService.hardDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryResponse> response = categoryService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getAllActive() {
        List<CategoryResponse> response = categoryService.getAllActive();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/simple")
    public ResponseEntity<List<CategoryResponse>> getAllActiveSimple() {
        List<CategoryResponse> response = categoryService.getAllActive();
        return ResponseEntity.ok(response);
    }
}
