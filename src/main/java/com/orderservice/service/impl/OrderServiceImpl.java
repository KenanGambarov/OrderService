package com.orderservice.service.impl;

import com.orderservice.client.ProductServiceClient;
import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.enums.RabbitQueueType;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.entity.OrderEntity;
import com.orderservice.entity.OrderItemEntity;
import com.orderservice.entity.OrderStatusHistoryEntity;
import com.orderservice.exception.ExceptionConstants;
import com.orderservice.mapper.OrderItemMapper;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.mapper.OrderStatusHistoryMapper;
import com.orderservice.queue.QueueSender;
import com.orderservice.repository.OrderRepository;
import com.orderservice.repository.OrderStatusHistoryRepository;
import com.orderservice.service.OrderCacheService;
import com.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.orderservice.exception.ExceptionConstants.ORDER_NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final ProductServiceClient productServiceClient;
    private final OrderCacheService cacheService;
    private final QueueSender queueSender;

    public OrderResponseDto getUserOrder(Long userId){
        OrderEntity order = cacheService.getOrderFromCacheOrDB(userId);
        return OrderMapper.toResponseDto(order);
    }

    @Transactional
    @Override
    public void creatOrder(OrderRequestDto orderRequestDto) {
        log.info("Order with Id: {} begin", orderRequestDto.getUserId());
        OrderEntity order = OrderMapper.toEntity(orderRequestDto);
        log.info("OrderEntity with Id: {} ", orderRequestDto.getUserId());
        List<OrderItemEntity> itemEntities = orderRequestDto.getOrderItems()
                .stream().map(o ->
                        {

                        Double price = productServiceClient.getProductById(o.getProductId()).getPrice();
                        log.info("Order with Id: {} price", price);
                        return OrderItemMapper.toEntity(o,order,price);
                        }).toList();
        log.info("itemEntities with Id: {} ", orderRequestDto.getUserId());

        order.setOrderItems(itemEntities);
        log.info("BigDecimal with Id: {} ", orderRequestDto.getUserId());

        BigDecimal totalAmount = itemEntities.stream()
                .map(OrderItemEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("setTotalAmount with Id: {} ", orderRequestDto.getUserId());

        order.setTotalAmount(totalAmount);
        log.info("save with Id: {} ", orderRequestDto.getUserId());

        orderRepository.save(order);
        log.info("Order with Id: {} created", order.getId());
        saveOrderHistory(order,null);
    }

    @Transactional
    @Override
    public void changeOrderStatus(Long userId, Long orderId, OrderStatus orderStatus) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND.getMessage()));

        if (order.getStatus() == OrderStatus.CANCELED
                || order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException(ExceptionConstants.ORDER_STATUS.getMessagePattern(order.getStatus()));
        }

//        some OrderStatus case should check

        saveOrderHistory(order,order.getStatus());
        order.setStatus(orderStatus);
        order.setStatusChangeDate(new Date());
        orderRepository.save(order);
        log.info("Order: {} status changed to: {}", order.getId(),order.getStatus());
        queueSender.sendOrderUpdate(RabbitQueueType.QUEUE_NAME.getQueueName(),OrderMapper.toRequestDto(userId));
        cacheService.clearOrderCache(userId);
    }

    private void saveOrderHistory(OrderEntity order, OrderStatus orderStatus) {
        historyRepository.save(
                OrderStatusHistoryMapper.toEntity(order,orderStatus)
        );
        log.info("OrderStatusHistory saved. orderId: {}, orderStatus: {}",
                order.getId(), order.getStatus());
    }


}
