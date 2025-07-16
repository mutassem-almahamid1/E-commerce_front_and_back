package com.mutassemalmahamid.ecommerce.repository.impl;

import com.mutassemalmahamid.ecommerce.model.document.Order;
import com.mutassemalmahamid.ecommerce.repository.OrderRepo;
import com.mutassemalmahamid.ecommerce.repository.mongo.OrderMongoRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepoImp implements OrderRepo {
    private final OrderMongoRepo orderMongoRepo;

    public OrderRepoImp(OrderMongoRepo orderMongoRepo) {
        this.orderMongoRepo = orderMongoRepo;
    }

    @Override
    public Order save(Order order) {
        return orderMongoRepo.save(order);
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        return orderMongoRepo.saveAll(orders);
    }

    @Override
    public Optional<Order> getByIdIfPresent(String id) {
        return orderMongoRepo.findById(id);
    }

    @Override
    public Order getByIdIgnoreStatus(String id) {
        return orderMongoRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void deleteById(String id) {
        orderMongoRepo.deleteById(id);
    }

    @Override
    public void delete(Order order) {
        orderMongoRepo.delete(order);
    }

    @Override
    public List<Order> getAllByUserId(String userId) {
        return orderMongoRepo.findAllByUserId(userId);
    }

    @Override
    public List<Order> getAll() {
        return orderMongoRepo.findAll();
    }

    @Override
    public List<Order> getAllIgnoreStatus() {
        return orderMongoRepo.findAll();
    }

    @Override
    public Page<Order> getAll(Pageable pageable) {
        return orderMongoRepo.findAll(pageable);
    }

    @Override
    public Page<Order> getAllByUserId(String userId, Pageable pageable) {
        return orderMongoRepo.findAllByUserId(userId, pageable);
    }



}
