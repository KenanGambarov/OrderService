package com.orderservice.repository;

import com.orderservice.entity.OrdersEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrdersEntity,Long> {

    @EntityGraph(attributePaths = {"orderItems"})
    Optional<OrdersEntity> findByIdAndUserId(Long orderId, Long userId);

    @EntityGraph(attributePaths = {"orderItems"})
    Optional<OrdersEntity> findByUserId(Long userId);

//    @EntityGraph(attributePaths = {"orderItems"})
//    Optional<OrdersEntity> findById(Long orderId);
}
