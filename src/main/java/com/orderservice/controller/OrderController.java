package com.orderservice.controller;

import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("v1/order/")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getUserOrder(@PathVariable Long userId){
        return orderService.getUserOrder(userId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void creatOrder(@RequestBody OrderRequestDto orderRequestDto){
        log.info("orderRequestDto getUserId {}",orderRequestDto.getUserId());
        orderService.creatOrder(orderRequestDto);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void changeOrderStatus(Long userId, Long orderId,@RequestBody OrderStatus orderStatus){
        orderService.changeOrderStatus(userId, orderId, orderStatus);
    }

}
