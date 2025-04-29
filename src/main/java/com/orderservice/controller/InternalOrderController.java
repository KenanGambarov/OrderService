package com.orderservice.controller;

import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("internal/v1/order/")
@Slf4j
public class InternalOrderController {


    private final OrderService orderService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getOrderById(@PathVariable("id") Long orderId){
        return orderService.getOrderById(orderId);
    }

}
