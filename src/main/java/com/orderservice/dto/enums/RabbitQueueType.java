package com.orderservice.dto.enums;

import lombok.Getter;

@Getter
public enum RabbitQueueType {

    QUEUE_NAME("ORDER_UPDATE");

    private final String queueName;

    RabbitQueueType(String queueName){
        this.queueName = queueName;
    }

}
