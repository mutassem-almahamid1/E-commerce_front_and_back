package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.model.document.Product;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.repository.ProductRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.ProductMongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepoImp implements ProductRepo {

    private final ProductMongoRepo productMongoRepo;

    public ProductRepoImp(ProductMongoRepo productMongoRepo) {
        this.productMongoRepo = productMongoRepo;
    }

    @Override
    public Product save(Product product) {
        return productMongoRepo.save(product);
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return productMongoRepo.saveAll(products);
    }

    @Override
    public Optional<Product> getByIdIfPresent(String id){
        return productMongoRepo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Optional<Product> getByNameIfPresent(String name){
        return productMongoRepo.findByNameAndStatus(name, Status.ACTIVE);
    }

    @Override
    public Page<Product> getAll(Pageable pageable){
        return productMongoRepo.findAllByStatus(Status.ACTIVE, pageable);
    }

    @Override
    public Page<Product> getAllByCategoryId(String categoryId, Pageable pageable) {
        return productMongoRepo.findAllByCategoryIdAndStatus(categoryId, Status.ACTIVE, pageable);
    }

    @Override
    public Page<Product> getAllByBrand(String brand, Pageable pageable) {
        return productMongoRepo.findAllByBrandAndStatus(brand, Status.ACTIVE, pageable);
    }

    @Override
    public Page<Product> searchByName(String name, Pageable pageable) {
        return productMongoRepo.findByNameContainingIgnoreCaseAndStatus(name, Status.ACTIVE, pageable);
    }

    @Override
    public List<Product> getNewestProducts() {
        // جلب أحدث 10 منتجات حسب تاريخ الإنشاء
        return productMongoRepo.findTop10ByStatusOrderByCreatedAtDesc(Status.ACTIVE);
    }

    @Override
    public List<Product> getTopRatedProducts() {
        // جلب أعلى 10 منتجات حسب التقييم
        return productMongoRepo.findTop10ByStatusOrderByAvgRatingDesc(Status.ACTIVE);
    }

    @Override
    public void deleteById(String productId) {
        productMongoRepo.deleteById(productId);
    }

    @Override
    public void delete(Product product) {
        productMongoRepo.delete(product);
    }
}
