package com.mutassemalmahamid.ecommerce.model.document;

import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static com.mutassemalmahamid.ecommerce.model.enums.OrderStatus.PENDING;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    private String id;
    private String userId;

    private Address shippingAddress;

    private double totalAmount;

    private List<OrderItem> items;

    private PaymentMethod paymentMethod;

    private LocalDateTime orderDate;

    private OrderStatus status = PENDING;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
