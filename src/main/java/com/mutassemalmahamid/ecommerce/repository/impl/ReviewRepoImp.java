package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.model.document.Review;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.ReviewRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.ReviewMongoRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepoImp implements ReviewRepo {

    private final ReviewMongoRepo reviewMongoRepo;

    public ReviewRepoImp(ReviewMongoRepo reviewMongoRepo) {
        this.reviewMongoRepo = reviewMongoRepo;
    }

    @Override
    public Review save(Review review) {
        return reviewMongoRepo.save(review);
    }

    @Override
    public List<Review> saveAll(List<Review> reviews) {
        return reviewMongoRepo.saveAll(reviews);
    }

    @Override
    public Optional<Review> getByIdIfPresent(String id) {
        return reviewMongoRepo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Review getByIdIgnoreStatus(String id) {
        return reviewMongoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @Override
    public List<Review> getAllByProductId(String productId) {
        return reviewMongoRepo.findAllByProductIdAndStatus(productId, Status.ACTIVE);
    }

    @Override
    public List<Review> getAllByUserId(String userId) {
        return reviewMongoRepo.findAllByUserIdAndStatus(userId, Status.ACTIVE);
    }

    @Override
    public Optional<Review> getByUserIdAndProductId(String userId, String productId) {
        return reviewMongoRepo.findByUserIdAndProductIdAndStatus(userId, productId, Status.ACTIVE);
    }

    @Override
    public double calculateAverageRatingForProduct(String productId) {
        List<Review> reviews = reviewMongoRepo.findAllByProductIdAndStatusForAverageRating(productId, Status.ACTIVE);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        return sum / reviews.size();
    }

    @Override
    public List<Review> getAll() {
        return reviewMongoRepo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public void deleteById(String id) {
        reviewMongoRepo.deleteById(id);
    }

    @Override
    public void delete(Review review) {
        reviewMongoRepo.delete(review);
    }
}
