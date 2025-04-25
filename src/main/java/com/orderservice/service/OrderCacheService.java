package com.orderservice.service;

import com.orderservice.entity.OrderEntity;

public interface OrderCacheService {

    OrderEntity getOrderFromCacheOrDB(Long userId);

    void clearOrderCache(Long userId);

}
