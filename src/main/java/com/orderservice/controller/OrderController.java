package com.orderservice.controller;

import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("v1/order/")
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getUserOrder(Long id){
        return orderService.getUserOrder(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void creatOrder(OrderRequestDto orderRequestDto){
        orderService.creatOrder(orderRequestDto);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void changeOrderStatus(Long userId, Long orderId, OrderStatus orderStatus){
        orderService.changeOrderStatus(userId, orderId, orderStatus);
    }

}
