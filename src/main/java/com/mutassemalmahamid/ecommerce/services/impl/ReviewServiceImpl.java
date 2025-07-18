package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.ReviewMapper;
import com.mutassemalmahamid.ecommerce.model.document.Review;
import com.mutassemalmahamid.ecommerce.model.dto.request.ReviewReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ReviewResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.repository.ReviewRepo;
import com.mutassemalmahamid.ecommerce.repository.UserRepo;
import com.mutassemalmahamid.ecommerce.services.ReviewService;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo, UserRepo userRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ReviewResponse create(ReviewReq request, String userId) {
        Review review = ReviewMapper.toEntity(request, userId);
        Review saved = reviewRepo.save(review);
        var user = userRepo.getById(saved.getUserId());
        return ReviewMapper.toResponse(saved, user);
    }

    @Override
    public ReviewResponse getById(String id) {
        Review review = reviewRepo.getById(id);
        var user = userRepo.getById(review.getUserId());
        return ReviewMapper.toResponse(review, user);
    }

    @Override
    public List<ReviewResponse> getAllByProductId(String productId) {
        return reviewRepo.getAllByProductId(productId).stream()
                .map(review -> {
                    var user = userRepo.getById(review.getUserId());
                    return ReviewMapper.toResponse(review, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getAllByUserId(String userId) {
        var user = userRepo.getById(userId);
        return reviewRepo.getAllByUserId(userId).stream()
                .map(review -> ReviewMapper.toResponse(review, user))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse update(String id, ReviewReq request) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        ReviewMapper.updateEntity(review, request);
        review.setUpdatedAt(LocalDateTime.now());
        Review updated = reviewRepo.save(review);
        var user = userRepo.getById(updated.getUserId());
        return ReviewMapper.toResponse(updated, user);
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        review.setStatus(Status.DELETED);
        review.setDeletedAt(LocalDateTime.now());
        reviewRepo.save(review);
        return AssistantHelper.toMessageResponse("Review soft deleted.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        reviewRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Review hard deleted.");
    }

    @Override
    public Page<ReviewResponse> getAll(Pageable pageable) {
        List<ReviewResponse> responses = reviewRepo.getAll().stream()
                .map(review -> {
                    var user = userRepo.getById(review.getUserId());
                    return ReviewMapper.toResponse(review, user);
                })
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responses.size());
        List<ReviewResponse> pageContent = responses.subList(start, end);
        return new PageImpl<>(pageContent, pageable, responses.size());
    }

    @Override
    public double getAverageRatingForProduct(String productId) {
        return reviewRepo.calculateAverageRatingForProduct(productId);
    }
}
