package com.orderservice.repository;

import com.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    @EntityGraph(attributePaths = {"orderItems"})
    Optional<OrderEntity> findByIdAndUserId(Long orderId, Long userId);

    @EntityGraph(attributePaths = {"orderItems"})
    Optional<OrderEntity> findByUserId(Long userId);
}
