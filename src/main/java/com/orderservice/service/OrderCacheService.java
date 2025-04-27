package com.orderservice.service;

import com.orderservice.entity.OrdersEntity;

import java.util.Optional;

public interface OrderCacheService {

    Optional<OrdersEntity> getOrderFromCacheOrDB(Long userId);

    void clearOrderCache(Long userId);

}
