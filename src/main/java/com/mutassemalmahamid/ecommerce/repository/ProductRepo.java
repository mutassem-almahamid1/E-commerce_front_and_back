package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepo {
    Product save(Product product);

    List<Product> saveAll(List<Product> products);

    Optional<Product> getByIdIfPresent(String id);

    Optional<Product> getByNameIfPresent(String name);

    Page<Product> getAll(Pageable pageable);

    Page<Product> getAllByCategoryId(String categoryId, Pageable pageable);

    Page<Product> getAllByBrand(String brand, Pageable pageable); // إضافة دالة للبحث حسب الماركة

    Page<Product> searchByName(String name, Pageable pageable); // إضافة دالة للبحث حسب الاسم

    List<Product> getNewestProducts(); // إضافة أحدث المنتجات

    List<Product> getTopRatedProducts(); // إضافة أعلى تقييماً

    void deleteById(String productId);

    void delete(Product product);
}
