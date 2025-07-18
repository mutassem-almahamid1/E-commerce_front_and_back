package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.ProductMapper;
import com.mutassemalmahamid.ecommerce.model.document.Product;
import com.mutassemalmahamid.ecommerce.model.dto.request.ProductReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ProductResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.repository.ProductRepo;
import com.mutassemalmahamid.ecommerce.services.ProductService;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.repository.ReviewRepo;
import com.mutassemalmahamid.ecommerce.model.document.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ReviewRepo reviewRepo;

    public ProductServiceImpl(ProductRepo productRepo, ReviewRepo reviewRepo) {
        this.productRepo = productRepo;
        this.reviewRepo = reviewRepo;
    }

    @Override
    public ProductResponse create(ProductReq request) {
        Product product = ProductMapper.toEntity(request);
        Product saved = productRepo.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return ProductMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(String id, ProductReq request) {
        Product product = productRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        ProductMapper.updateEntity(product, request);
        product.setUpdatedAt(LocalDateTime.now());
        Product updated = productRepo.save(product);
        return ProductMapper.toResponse(updated);
    }

    @Override
    public MessageResponse updateStatus(String id, Status status) {
        Product product = productRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
        return AssistantHelper.toMessageResponse("Status updated successfully.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        Product product = productRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setStatus(Status.DELETED);
        product.setDeletedAt(LocalDateTime.now());
        productRepo.save(product);
        return AssistantHelper.toMessageResponse("Product soft deleted.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        productRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Product hard deleted.");
    }

    @Override
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepo.getAll(pageable).map(product -> {
            double avgRating = 0.0;
            int reviewCount = 0;
            var reviews = reviewRepo.getAllByProductId(product.getId());
            if (reviews != null && !reviews.isEmpty()) {
                reviewCount = reviews.size();
                avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            }
            return ProductMapper.toResponse(product, avgRating, reviewCount);
        });
    }

    @Override
    public Page<ProductResponse> getByCategory(String categoryId, Pageable pageable) {
        return productRepo.getAllByCategoryId(categoryId, pageable).map(product -> {
            double avgRating = 0.0;
            int reviewCount = 0;
            var reviews = reviewRepo.getAllByProductId(product.getId());
            if (reviews != null && !reviews.isEmpty()) {
                reviewCount = reviews.size();
                avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            }
            return ProductMapper.toResponse(product, avgRating, reviewCount);
        });
    }

    @Override
    public Page<ProductResponse> getByBrand(String brand, Pageable pageable) {
        return productRepo.getAllByBrand(brand, pageable).map(product -> {
            double avgRating = 0.0;
            int reviewCount = 0;
            var reviews = reviewRepo.getAllByProductId(product.getId());
            if (reviews != null && !reviews.isEmpty()) {
                reviewCount = reviews.size();
                avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            }
            return ProductMapper.toResponse(product, avgRating, reviewCount);
        });
    }

    @Override
    public Page<ProductResponse> searchByName(String name, Pageable pageable) {
        return productRepo.searchByName(name, pageable).map(product -> {
            double avgRating = 0.0;
            int reviewCount = 0;
            var reviews = reviewRepo.getAllByProductId(product.getId());
            if (reviews != null && !reviews.isEmpty()) {
                reviewCount = reviews.size();
                avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            }
            return ProductMapper.toResponse(product, avgRating, reviewCount);
        });
    }

    @Override
    public List<ProductResponse> getNewestProducts() {
        return productRepo.getNewestProducts().stream().map(product -> {
            double avgRating = 0.0;
            int reviewCount = 0;
            var reviews = reviewRepo.getAllByProductId(product.getId());
            if (reviews != null && !reviews.isEmpty()) {
                reviewCount = reviews.size();
                avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            }
            return ProductMapper.toResponse(product, avgRating, reviewCount);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getTopRatedProducts() {
        return productRepo.getTopRatedProducts().stream().map(product -> {
            double avgRating = 0.0;
            int reviewCount = 0;
            var reviews = reviewRepo.getAllByProductId(product.getId());
            if (reviews != null && !reviews.isEmpty()) {
                reviewCount = reviews.size();
                avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            }
            return ProductMapper.toResponse(product, avgRating, reviewCount);
        }).collect(Collectors.toList());
    }
}
