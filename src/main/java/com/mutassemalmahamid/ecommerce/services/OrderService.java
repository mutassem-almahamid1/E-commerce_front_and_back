package com.mutassemalmahamid.ecommerce.services;

import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    /**
     * إنشاء طلب جديد
     */
    OrderResponse create(String userId, OrderRequest request);

    /**
     * جلب طلب بالمعرف
     */
    OrderResponse getById(String id);

    /**
     * جلب جميع طلبات المستخدم
     */
    List<OrderResponse> getAllByUserId(String userId);

    /**
     * جلب جميع الطلبات مع pagination
     */
    Page<OrderResponse> getAll(Pageable pageable);

    /**
     * تحديث حالة الطلب
     */
    OrderResponse updateStatus(String orderId, OrderStatus newStatus);

    /**
     * إلغاء الطلب
     */
    void cancelOrder(String orderId);
}
