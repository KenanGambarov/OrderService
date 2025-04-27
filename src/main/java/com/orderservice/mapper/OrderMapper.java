package com.orderservice.mapper;

import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.enums.PaymentStatus;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.entity.OrdersEntity;

import java.util.Date;

public class OrderMapper {

    public static OrdersEntity toEntity(OrderRequestDto orderRequestDto) {
        return OrdersEntity.builder()
                .userId(orderRequestDto.getUserId())
                .status(OrderStatus.PENDING)
                .orderDate(new Date())
                .shippingAddress(orderRequestDto.getShippingAddress())
                .paymentStatus(PaymentStatus.PENDING)
                .statusChangeDate(new Date())
                .build();
    }

    public static OrderResponseDto toResponseDto(OrdersEntity order) {
        return OrderResponseDto.builder()
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .paymentStatus(order.getPaymentStatus())
                .shippingAddress(order.getShippingAddress())
                .orderItems(OrderItemMapper.toResponseDto(order.getOrderItems()))
                .build();
    }

    public static OrderRequestDto toRequestDto(Long userId){
        return OrderRequestDto.builder()
                .userId(userId)
                .build();
    }
}
