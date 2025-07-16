package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.ReviewReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ReviewResponse;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    ReviewResponse create(ReviewReq request, String userId);
    ReviewResponse getById(String id);
    List<ReviewResponse> getAllByProductId(String productId);
    List<ReviewResponse> getAllByUserId(String userId);
    ReviewResponse update(String id, ReviewReq request);
    MessageResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
    Page<ReviewResponse> getAll(Pageable pageable);
    double getAverageRatingForProduct(String productId);
}
