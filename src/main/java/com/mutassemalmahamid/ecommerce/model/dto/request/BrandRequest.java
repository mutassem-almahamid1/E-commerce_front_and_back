package com.mutassemalmahamid.ecommerce.model.dto.request;

import com.mutassemalmahamid.ecommerce.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {

    @NotBlank(message = "Brand name cannot be blank")
    private String name;

    private String description;

    private String logoUrl;

    private String websiteUrl;

    private Status status;
}

