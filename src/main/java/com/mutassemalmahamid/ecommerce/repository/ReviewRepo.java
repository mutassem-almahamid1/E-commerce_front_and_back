package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepo {
    Review save(Review review);

    List<Review> saveAll(List<Review> reviews);

    Optional<Review> getByIdIfPresent(String id);

    Review getByIdIgnoreStatus(String id); // تغيير نوع الإرجاع

    List<Review> getAllByProductId(String productId);

    // تغيير لإرجاع قائمة بدلاً من عنصر واحد
    List<Review> getAllByUserId(String userId);

    // إضافة دالة للبحث باستخدام المستخدم والمنتج معًا
    Optional<Review> getByUserIdAndProductId(String userId, String productId);

    // إضافة دالة لحساب متوسط التقييم للمنتج
    double calculateAverageRatingForProduct(String productId);

    List<Review> getAll();

    void deleteById(String id);

    void delete(Review review);

    Review getById(String id);
}
