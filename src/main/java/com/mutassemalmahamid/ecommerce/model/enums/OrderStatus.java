package com.mutassemalmahamid.ecommerce.model.enums;

public enum OrderStatus {
    PENDING,     // قيد الانتظار
    PROCESSING,  // قيد التجهيز
    SHIPPED,     // تم الشحن
    DELIVERED,   // تم التوصيل
    CANCELLED,
    RETURNED,
    CONFIRMED
}
