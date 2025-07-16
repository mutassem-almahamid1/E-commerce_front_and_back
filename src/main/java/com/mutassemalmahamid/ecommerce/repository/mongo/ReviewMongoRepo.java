package com.mutassemalmahamid.ecommerce.repository.mongo;

import com.mutassemalmahamid.ecommerce.model.document.Review;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewMongoRepo extends MongoRepository<Review, String> {
    Optional<Review> findByIdAndStatus(String id, Status status);

    List<Review> findAllByStatus(Status status);

    Page<Review> findAllByStatus(Status status, Pageable pageable);

    // تصحيح اسم الدالة لتتناسب مع اسم الحقل userId
    List<Review> findAllByUserIdAndStatus(String userId, Status status);

    Page<Review> findAllByUserIdAndStatus(String userId, Status status, Pageable pageable);

    List<Review> findAllByProductIdAndStatus(String productId, Status status);

    // إضافة دالة للبحث عن المراجعات باستخدام المستخدم والمنتج معًا
    Optional<Review> findByUserIdAndProductIdAndStatus(String userId, String productId, Status status);

    // إضافة دالة لحساب متوسط التقييم للمنتج
    @Query("{ 'productId' : ?0, 'status' : ?1 }")
    List<Review> findAllByProductIdAndStatusForAverageRating(String productId, Status status);
}
