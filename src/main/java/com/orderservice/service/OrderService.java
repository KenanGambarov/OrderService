package com.orderservice.service;

import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderResponseDto;

public interface OrderService {

    OrderResponseDto getUserOrder(Long userId);

    void creatOrder(OrderRequestDto orderRequestDto);

    void changeOrderStatus(Long userId, Long orderId, OrderStatus orderStatus);

}
