package com.orderservice.service.impl;

import com.orderservice.client.ProductServiceClient;
import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.enums.RabbitQueueType;
import com.orderservice.dto.request.OrderRequestDto;
import com.orderservice.dto.response.OrderResponseDto;
import com.orderservice.entity.OrdersEntity;
import com.orderservice.entity.OrderItemEntity;
import com.orderservice.exception.ExceptionConstants;
import com.orderservice.exception.NotFoundException;
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
        OrdersEntity order = cacheService.getOrderFromCacheOrDB(userId).orElseThrow(()-> new NotFoundException(ExceptionConstants.ORDER_NOT_FOUND.getMessage()));
        return OrderMapper.toResponseDto(order);
    }

    @Transactional
    @Override
    public void creatOrder(OrderRequestDto orderRequestDto) {
        log.info("Order with Id: {} begin", orderRequestDto.getUserId());
        OrdersEntity order = OrderMapper.toEntity(orderRequestDto);
        log.info("OrderEntity with status: {} ", order.getStatus());
        List<OrderItemEntity> itemEntities = orderRequestDto.getOrderItems()
                .stream().map(o ->
                        {
                        Double price = productServiceClient.getProductById(o.getProductId()).getPrice();
                        log.info("Order with Id: {} price", price);
                        return OrderItemMapper.toEntity(o,order,price, BigDecimal.valueOf(price*o.getQuantity()));
                        }).toList();
        log.info("OrderEntity with status2: {} ", order.getStatus());

        order.setOrderItems(itemEntities);
        log.info("OrderEntity with status3: {} ", order.getStatus());

        BigDecimal totalAmount = itemEntities.stream()
                .map(item -> item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("OrderEntity with status4: {} ", order.getStatus());

        order.setTotalAmount(totalAmount);
        log.info("OrderEntity with status5: {} ", order.getStatus());

        orderRepository.save(order);
        log.info("OrderEntity with status6: {} ", order.getStatus());
        saveOrderHistory(order,null);
        queueSender.sendOrderUpdate(RabbitQueueType.QUEUE_NAME.getQueueName(),OrderMapper.toRequestDto(order.getUserId()));
    }

    @Transactional
    @Override
    public void changeOrderStatus(Long userId, Long orderId, OrderStatus orderStatus) {
        OrdersEntity order = orderRepository.findByIdAndUserId(orderId, userId)
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
//        queueSender.sendOrderUpdate(RabbitQueueType.QUEUE_NAME.getQueueName(),OrderMapper.toRequestDto(userId));
        cacheService.clearOrderCache(userId);
    }

    private void saveOrderHistory(OrdersEntity order, OrderStatus orderStatus) {
        historyRepository.save(
                OrderStatusHistoryMapper.toEntity(order,orderStatus)
        );
        log.info("OrderStatusHistory saved. orderId: {}, orderStatus: {}",
                order.getId(), order.getStatus());
    }


}
