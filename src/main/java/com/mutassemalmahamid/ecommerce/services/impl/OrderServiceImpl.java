package com.mutassemalmahamid.ecommerce.services.impl;

import com.mutassemalmahamid.ecommerce.handelException.exception.NotFoundException;
import com.mutassemalmahamid.ecommerce.mapper.OrderMapper;
import com.mutassemalmahamid.ecommerce.mapper.OrderItemMapper;
import com.mutassemalmahamid.ecommerce.model.document.Order;
import com.mutassemalmahamid.ecommerce.model.document.OrderItem;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderItemRequest;
import com.mutassemalmahamid.ecommerce.model.dto.request.OrderRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.OrderResponse;
import com.mutassemalmahamid.ecommerce.model.enums.OrderStatus;
import com.mutassemalmahamid.ecommerce.repository.OrderRepo;
import com.mutassemalmahamid.ecommerce.repository.ProductRepo;
import com.mutassemalmahamid.ecommerce.services.OrderService;
import com.mutassemalmahamid.ecommerce.model.document.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    private static final double TAX_RATE = 0.10; // 10% ضريبة
    private static final double SHIPPING_COST = 15.0; // تكلفة شحن ثابتة

    @Autowired
    public OrderServiceImpl(OrderRepo orderRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    @Override
    public OrderResponse create(String userId, OrderRequest request) {
        // حساب مبالغ الطلب
        OrderCalculation calculation = calculateOrderAmounts(request.getItems(), request.getCouponCode());

        // إنشاء الطلب
        Order order = OrderMapper.toEntity(request, userId);
        order.setItems(OrderItemMapper.toEntityList(request.getItems(), productRepo));
        order.setTotalAmount(calculation.totalAmount());
        order.setDiscountAmount(calculation.discountAmount());
        order.setTaxAmount(calculation.taxAmount());
        order.setShippingCost(calculation.shippingCost());
        order.setFinalAmount(calculation.finalAmount());

        // حفظ الطلب
        Order saved = orderRepo.save(order);
        return OrderMapper.toResponse(saved, productRepo);
    }

    @Override
    public OrderResponse getById(String id) {
        Order order = orderRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
        return OrderMapper.toResponse(order, productRepo);
    }

    @Override
    public List<OrderResponse> getAllByUserId(String userId) {
        return orderRepo.getAllByUserId(userId).stream()
                .map(order -> OrderMapper.toResponse(order, productRepo))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> getAll(Pageable pageable) {
        return orderRepo.getAll(pageable)
                .map(order -> OrderMapper.toResponse(order, productRepo));
    }

    @Override
    public OrderResponse updateStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepo.getByIdIfPresent(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        // التحقق من صحة تغيير الحالة
        validateStatusTransition(order.getStatus(), newStatus);

        // تحديث الحالة والتوقيت المناسب
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (newStatus == OrderStatus.SHIPPED) {
            order.setShippedAt(LocalDateTime.now());
        } else if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        Order updated = orderRepo.save(order);
        return OrderMapper.toResponse(updated, productRepo);
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepo.getByIdIfPresent(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Cannot cancel order that is already shipped or delivered");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);
    }

    private OrderCalculation calculateOrderAmounts(List<OrderItemRequest> items, String couponCode) {
        double totalAmount = items.stream()
                .mapToDouble(item -> {
                    Product product = productRepo.getByIdIfPresent(item.getProductId())
                            .orElseThrow(() -> new NotFoundException("Product not found: " + item.getProductId()));
                    return product.getPrice() * item.getQuantity();
                })
                .sum();

        double discountAmount = calculateDiscount(totalAmount, couponCode);
        double taxAmount = (totalAmount - discountAmount) * TAX_RATE;
        double shippingCost = totalAmount > 100 ? 0 : SHIPPING_COST; // شحن مجاني للطلبات فوق 100
        double finalAmount = totalAmount - discountAmount + taxAmount + shippingCost;

        return new OrderCalculation(totalAmount, discountAmount, taxAmount, shippingCost, finalAmount);
    }

    private double calculateDiscount(double totalAmount, String couponCode) {
        // منطق حساب الخصم حسب كود الكوبون
        if (couponCode != null && !couponCode.isEmpty()) {
            switch (couponCode.toUpperCase()) {
                case "SAVE10":
                    return totalAmount * 0.10;
                case "SAVE20":
                    return totalAmount * 0.20;
                case "FIRST50":
                    return Math.min(50, totalAmount * 0.15);
                default:
                    return 0;
            }
        }
        return 0;
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from PENDING to " + newStatus);
                }
                break;
            case CONFIRMED:
                if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from CONFIRMED to " + newStatus);
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from PROCESSING to " + newStatus);
                }
                break;
            case SHIPPED:
                if (newStatus != OrderStatus.DELIVERED && newStatus != OrderStatus.RETURNED) {
                    throw new IllegalStateException("Invalid status transition from SHIPPED to " + newStatus);
                }
                break;
            case DELIVERED:
                if (newStatus != OrderStatus.RETURNED) {
                    throw new IllegalStateException("Invalid status transition from DELIVERED to " + newStatus);
                }
                break;
            case CANCELLED:
            case RETURNED:
                throw new IllegalStateException("Cannot change status from " + currentStatus);
            default:
                throw new IllegalStateException("Unknown status: " + currentStatus);
        }
    }

    // فئة مساعدة لحساب مبالغ الطلب
    private record OrderCalculation(
            double totalAmount,
            double discountAmount,
            double taxAmount,
            double shippingCost,
            double finalAmount
    ) {}
}
