package com.mutassemalmahamid.ecommerce.services.impl;

public record OrderCalculation(
    double totalAmount,
    double discountAmount,
    double taxAmount,
    double shippingCost,
    double finalAmount
) {}
