package com.orderservice.service;

import com.orderservice.dto.request.OrderRequestDto;

public interface OrderService {

    void creatOrder(OrderRequestDto orderRequestDto);

    void orderCanceled(Long userId, Long orderId);

}
