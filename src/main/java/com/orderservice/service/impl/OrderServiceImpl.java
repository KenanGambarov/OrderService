package com.orderservice.service.impl;

import com.orderservice.client.ProductServiceClient;
import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.enums.PaymentStatus;
import com.orderservice.dto.product.ProductDto;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderItemResponseDto;
import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.entity.OrderEntity;
import com.orderservice.entity.OrderItemEntity;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private ProductServiceClient productServiceClient;

    private OrderResponseDto getUserOrder(Long userId){
        OrderEntity order = orderRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Sifariş tapılmadı və ya icazə yoxdur"));
        return OrderResponseDto.builder()
                    .status(order.getStatus())
                    .orderDate(order.getOrderDate())
                    .paymentStatus(order.getPaymentStatus())
                    .shippingAddress(order.getShippingAddress())
                    .orderItems(order.getOrderItems().stream().map(e -> OrderItemResponseDto.builder()
                            .price(e.getPrice())
                            .productId(e.getProductId())
                            .quantity(e.getQuantity())
                            .totalPrice(e.getTotalPrice())
                            .build()).toList())
                    .build();

    }

    @Transactional
    @Override
    public void creatOrder(OrderRequestDto orderRequestDto) {
        OrderEntity order = OrderEntity.builder()
                .userId(orderRequestDto.getUserId())
                .status(OrderStatus.CONFIRMED)
                .orderDate(new Date())
                .shippingAddress(orderRequestDto.getShippingAddress())
                .paymentStatus(PaymentStatus.PAID)
                .statusChangeDate(new Date())
                .build();

        List<OrderItemEntity> itemEntities = orderRequestDto.getOrderItems()
                .stream().map(o ->
                        {
                        Double price = productServiceClient.getProductById(o.getProductId()).getPrice();
                        return OrderItemEntity.builder()
                        .order(order)
                        .productId(o.getProductId())
                        .quantity(o.getQuantity())
                        .price(price)
                        .build();
                        }).toList();
        order.setOrderItems(itemEntities);

        BigDecimal totalAmount = itemEntities.stream()
                .map(OrderItemEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

    }

    @Override
    public void orderCanceled(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Sifariş tapılmadı və ya icazə yoxdur"));

        if (order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Bu sifariş artıq ləğv olunub və ya tamamlanıb");
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setStatusChangeDate(new Date());
        orderRepository.save(order);
    }


}
