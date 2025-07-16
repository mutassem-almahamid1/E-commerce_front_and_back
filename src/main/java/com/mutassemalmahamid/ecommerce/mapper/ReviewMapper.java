package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.model.document.Review;
import com.mutassemalmahamid.ecommerce.model.dto.request.ReviewReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ReviewResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;

import java.time.LocalDateTime;

public class ReviewMapper {

    public static Review toEntity(ReviewReq reviewReq, String userId) {
        return Review.builder()
                .userId(userId)
                .productId(reviewReq.getProductId())
                .comment(reviewReq.getComment())
                .rating(reviewReq.getRating())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .productId(review.getProductId())
                .comment(review.getComment())
                .rating(review.getRating())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .deletedAt(review.getDeletedAt())
                .build();
    }

    public static void updateEntity(Review review, ReviewReq reviewReq) {
        review.setRating(reviewReq.getRating());
        review.setComment(reviewReq.getComment());
        review.setUpdatedAt(LocalDateTime.now());
    }
}
