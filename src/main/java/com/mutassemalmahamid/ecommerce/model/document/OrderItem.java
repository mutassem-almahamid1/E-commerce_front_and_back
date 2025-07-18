package com.mutassemalmahamid.ecommerce.model.document;

import com.mutassemalmahamid.ecommerce.model.enums.ItemStatus;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String productId;
    private int quantity;
    private double price; // سعر المنتج وقت الطلب
    private double totalPrice; // السعر الإجمالي للكمية
    private OrderStatus status; // حالة العنصر في الطلب
    private String productName; // اسم المنتج للحفظ التاريخي
    private String productImageUrl; // صورة المنتج للحفظ التاريخي
}
