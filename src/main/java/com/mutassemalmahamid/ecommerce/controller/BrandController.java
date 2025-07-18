package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.model.dto.request.BrandRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.BrandResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest request) {
        BrandResponse response = brandService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(@PathVariable String id) {
        BrandResponse response = brandService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> update(@PathVariable String id, @Valid @RequestBody BrandRequest request) {
        BrandResponse response = brandService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable String id, @RequestParam Status status) {
        MessageResponse response = brandService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> softDelete(@PathVariable String id) {
        MessageResponse response = brandService.softDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<MessageResponse> hardDelete(@PathVariable String id) {
        MessageResponse response = brandService.hardDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BrandResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BrandResponse> response = brandService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<BrandResponse>> getAllActive(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BrandResponse> response = brandService.getAllActive(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/simple")
    public ResponseEntity<List<BrandResponse>> getAllActiveSimple() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<BrandResponse> page = brandService.getAllActive(pageable);
        return ResponseEntity.ok(page.getContent());
    }
}
