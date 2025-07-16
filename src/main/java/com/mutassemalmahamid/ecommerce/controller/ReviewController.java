package com.mutassemalmahamid.ecommerce.controller;

import com.mutassemalmahamid.ecommerce.model.dto.request.ReviewReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ReviewResponse;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewReq request,
                                                 @RequestParam String userId) {
        ReviewResponse response = reviewService.create(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable String id) {
        ReviewResponse response = reviewService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getAllByProductId(@PathVariable String productId) {
        List<ReviewResponse> response = reviewService.getAllByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getAllByUserId(@PathVariable String userId) {
        List<ReviewResponse> response = reviewService.getAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@PathVariable String id,@Valid @RequestBody ReviewReq request) {
        ReviewResponse response = reviewService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> softDelete(@PathVariable String id) {
        MessageResponse response = reviewService.softDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<MessageResponse> hardDelete(@PathVariable String id) {
        MessageResponse response = reviewService.hardDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> response = reviewService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForProduct(@PathVariable String productId) {
        double avg = reviewService.getAverageRatingForProduct(productId);
        return ResponseEntity.ok(avg);
    }
}

