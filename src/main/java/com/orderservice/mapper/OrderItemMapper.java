package com.orderservice.mapper;

import com.orderservice.dto.request.OrderItemRequestDto;
import com.orderservice.dto.response.OrderItemResponseDto;
import com.orderservice.entity.OrdersEntity;
import com.orderservice.entity.OrderItemEntity;

import java.math.BigDecimal;
import java.util.List;

public class OrderItemMapper {

    public static OrderItemEntity toEntity(OrderItemRequestDto itemRequestDto, OrdersEntity order, Double price, BigDecimal totalPrice){
        return OrderItemEntity.builder()
                .orders(order)
                .productId(itemRequestDto.getProductId())
                .quantity(itemRequestDto.getQuantity())
                .price(price)
                .totalPrice(totalPrice)
                .build();
    }

    public static List<OrderItemResponseDto> toResponseDto(List<OrderItemEntity> orderItems){
        return orderItems.stream().map(e -> OrderItemResponseDto.builder()
                .price(e.getPrice())
                .productId(e.getProductId())
                .quantity(e.getQuantity())
                .totalPrice(e.getTotalPrice())
                .build()).toList();
    }
}
