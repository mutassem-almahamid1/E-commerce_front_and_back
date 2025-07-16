package com.mutassemalmahamid.ecommerce.model.dto.request;

import com.mutassemalmahamid.ecommerce.model.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotEmpty
    private List<OrderItemRequest> items;

    @NotNull
    @Valid
    private AddressRequest shippingAddress;
    
    @NotNull
    private PaymentMethod paymentMethod;
}
