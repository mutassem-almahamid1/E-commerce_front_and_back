package com.mutassemalmahamid.ecommerce.repository;

import com.mutassemalmahamid.ecommerce.model.document.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepo {
    Order save(Order order);

    List<Order> saveAll(List<Order> orders);

    Optional<Order> getByIdIfPresent(String id);

    Order getByIdIgnoreStatus(String id);

    void deleteById(String id);

    void delete(Order order);

    List<Order> getAllByUserId(String userId);

    List<Order> getAll();

    List<Order> getAllIgnoreStatus();

    Page<Order> getAll(Pageable pageable);

    Page<Order> getAllByUserId(String userId, Pageable pageable);

}
