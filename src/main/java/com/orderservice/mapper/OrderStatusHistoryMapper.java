package com.orderservice.mapper;

import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.entity.OrderEntity;
import com.orderservice.entity.OrderStatusHistoryEntity;

public class OrderStatusHistoryMapper {

    public static OrderStatusHistoryEntity toEntity(OrderEntity order, OrderStatus orderStatus) {
        return OrderStatusHistoryEntity.builder()
                .orderId(order)
                .oldStatus(orderStatus)
                .newStatus(order.getStatus())
                .build();
    }
}
