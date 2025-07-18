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
    private String orderNumber; // رقم الطلب الفريد

    private Address shippingAddress;
    private Address billingAddress; // عنوان الفوترة

    private double totalAmount;
    private double shippingCost; // تكلفة الشحن
    private double taxAmount; // ضريبة
    private double discountAmount; // خصم
    private double finalAmount; // المبلغ النهائي

    private List<OrderItem> items;

    private PaymentMethod paymentMethod;
    private String paymentId; // معرف الدفع من الخدمة الخارجية
    private boolean isPaid; // حالة الدفع

    private OrderStatus status = PENDING;
    private String notes; // ملاحظات على الطلب
    private String trackingNumber; // رقم التتبع

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime shippedAt; // تاريخ الشحن
    private LocalDateTime deliveredAt; // تاريخ التسليم
}
