package com.orderservice.service.impl;

import com.orderservice.entity.OrdersEntity;
import com.orderservice.exception.ExceptionConstants;
import com.orderservice.exception.NotFoundException;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.OrderCacheService;
import com.orderservice.util.CacheUtil;
import com.orderservice.util.constraints.OrderCacheConstraints;
import com.orderservice.util.constraints.OrderCacheDurationConstraints;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class OrderCacheServiceImpl implements OrderCacheService {

    private final OrderRepository orderRepository;
    private final CacheUtil cacheUtil;

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackOrderCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackOrderCache")
    public Optional<OrdersEntity> getOrderFromCacheOrDB(Long userId) {
        OrdersEntity ordersEntity = cacheUtil.getOrLoad(OrderCacheConstraints.ORDER_KEY.getKey(userId),
                () -> orderRepository.findByUserId(userId).orElse(null),
                OrderCacheDurationConstraints.DAY.toDuration());
        return Optional.ofNullable(ordersEntity);
    }

    public Optional fallbackStockCache(Long userId, Throwable t) {
        log.error("Redis not available for userId {}, falling back to DB. Error: {}",userId, t.getMessage());
        return  Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackClearOrderCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackClearOrderCache")
    public void clearOrderCache(Long userId) {
        cacheUtil.deleteFromCache(OrderCacheConstraints.ORDER_KEY.getKey(userId));
        log.debug("Cache cleared for userId {}",  userId);
    }

    public void fallbackClearOrderCache(Long userId, Throwable t) {
        log.warn("Redis not available to clear cache for product {}, ignoring. Error: {}", userId, t.getMessage());
    }

}
