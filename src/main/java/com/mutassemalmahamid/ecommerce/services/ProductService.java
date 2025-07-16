package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.ProductReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.ProductResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductReq request);
    ProductResponse getById(String id);
    ProductResponse update(String id, ProductReq request);
    MessageResponse updateStatus(String id, Status status);
    MessageResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
    Page<ProductResponse> getAll(Pageable pageable);
    Page<ProductResponse> getByCategory(String categoryId, Pageable pageable);
    Page<ProductResponse> getByBrand(String brand, Pageable pageable);
    Page<ProductResponse> searchByName(String name, Pageable pageable);
    List<ProductResponse> getNewestProducts();
    List<ProductResponse> getTopRatedProducts();
}

