package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.BrandMapper;
import com.mutassemalmahamid.ecommerce.model.document.Brand;
import com.mutassemalmahamid.ecommerce.model.dto.request.BrandRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.BrandResponse;
import com.mutassemalmahamid.ecommerce.model.enums.Status;
import com.mutassemalmahamid.ecommerce.model.common.MessageResponse;
import com.mutassemalmahamid.ecommerce.repository.BrandRepo;
import com.mutassemalmahamid.ecommerce.services.BrandService;
import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepo brandRepo;

    public BrandServiceImpl(BrandRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    @Override
    public BrandResponse create(BrandRequest request) {
        Brand brand = BrandMapper.toEntity(request);
        Brand saved = brandRepo.save(brand);
        return BrandMapper.toResponse(saved);
    }

    @Override
    public BrandResponse getById(String id) {
        Brand brand = brandRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        return BrandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse update(String id, BrandRequest request) {
        Brand brand = brandRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        BrandMapper.updateEntity(brand, request);
        brand.setUpdatedAt(LocalDateTime.now());
        Brand updated = brandRepo.save(brand);
        return BrandMapper.toResponse(updated);
    }

    @Override
    public MessageResponse updateStatus(String id, Status status) {
        Brand brand = brandRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        brand.setStatus(status);
        brand.setUpdatedAt(LocalDateTime.now());
        brandRepo.save(brand);
        return AssistantHelper.toMessageResponse("Status updated successfully.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        Brand brand = brandRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        brand.setStatus(Status.DELETED);
        brand.setDeletedAt(LocalDateTime.now());
        brandRepo.save(brand);
        return AssistantHelper.toMessageResponse("Brand soft deleted.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        brandRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Brand hard deleted.");
    }

    @Override
    public Page<BrandResponse> getAll(Pageable pageable) {
        return brandRepo.getAll(pageable).map(BrandMapper::toResponse);
    }

    @Override
    public Page<BrandResponse> getAllActive(Pageable pageable) {
        return brandRepo.getAll(pageable).map(BrandMapper::toResponse);
    }
}

