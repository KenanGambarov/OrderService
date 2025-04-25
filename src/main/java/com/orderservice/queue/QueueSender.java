package com.orderservice.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.exception.QueueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class QueueSender {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;


    public <T> void sendOrderUpdate(String queueName, T dto) {
        try {
            amqpTemplate.convertAndSend(queueName, objectMapper.writeValueAsString(dto));
            log.info("Sent order {} message: {}",queueName, objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
             log.error("Sending order in invalid format: {}", e.getMessage());
        }catch (Exception e) {
            throw new QueueException();
        }

    }

}
