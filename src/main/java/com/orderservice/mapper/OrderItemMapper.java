package com.orderservice.mapper;

import com.orderservice.dto.request.OrderItemRequestDto;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderItemResponseDto;
import com.orderservice.entity.OrderEntity;
import com.orderservice.entity.OrderItemEntity;

import java.util.List;

public class OrderItemMapper {

    public static OrderItemEntity toEntity(OrderItemRequestDto itemRequestDto, OrderEntity order,Double price){
        return OrderItemEntity.builder()
                .order(order)
                .productId(itemRequestDto.getProductId())
                .quantity(itemRequestDto.getQuantity())
                .price(price)
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
