package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.BrandRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.BrandResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    BrandResponse create(BrandRequest request);
    BrandResponse getById(String id);
    BrandResponse update(String id, BrandRequest request);
    MessageResponse updateStatus(String id, Status status);
    MessageResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
    Page<BrandResponse> getAll(Pageable pageable);
    Page<BrandResponse> getAllActive(Pageable pageable);
}

