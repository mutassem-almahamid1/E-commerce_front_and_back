package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.CategoryReq;
import com.mutassemalmahamid.ecommerce.model.dto.response.CategoryResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryReq request);
    CategoryResponse getById(String id);
    CategoryResponse update(String id, CategoryReq request);
    MessageResponse updateStatus(String id, Status status);
    MessageResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
    Page<CategoryResponse> getAll(Pageable pageable);
    List<CategoryResponse> getAllActive();
}

