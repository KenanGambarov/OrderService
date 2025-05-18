package com.orderservice.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.request.PaymentRequestDto;
import com.orderservice.exception.QueueException;
import com.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {


    private final static String QUEUE_NAME="PAYMENT_UPDATE";
    private final ObjectMapper objectMapper;
    private final OrderService orderService;


    @RabbitListener(queues = QUEUE_NAME)
    public void consume(String message){
        try {
            log.info("consume Payment begin");
            var data = objectMapper.readValue(message, PaymentRequestDto.class);
            log.info("consume Payment begin userId {}" , data.getUserId());
            orderService.changePaymentStatus(data);
        } catch (JsonProcessingException e) {
            log.error("Consume message invalid format: {}", e.getMessage());
        }catch (Exception ex){
            throw new QueueException();
        }
    }

}
