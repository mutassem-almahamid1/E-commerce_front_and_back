package com.mutassemalmahamid.ecommerce.model.dto.request;

import com.mutassemalmahamid.ecommerce.model.document.Address;
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
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;

    @NotNull(message = "Shipping address is required")
    @Valid
    private Address shippingAddress;

    private Address billingAddress; // اختياري

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String notes; // ملاحظات اختيارية
    private String couponCode; // كود خصم اختياري
}

