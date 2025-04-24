package com.orderservice.repository;

import com.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderEntity> findByIdAndUserId(Long orderId, Long userId);

    Optional<OrderEntity> findByUserId(Long userId);
}
