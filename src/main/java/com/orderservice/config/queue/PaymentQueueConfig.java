package com.orderservice.config.queue;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentQueueConfig {

    private final String paymentQ;
    private final String paymentDLQ;
    private final String paymentQExchange;
    private final String paymentDLQExchange;
    private final String paymentQKey;
    private final String paymentDLQKey;

    public PaymentQueueConfig(@Value("${rabbitmq.payment-service.queue}") String paymentQ,
                              @Value("${rabbitmq.payment-service.dlq}") String paymentDLQ) {

        this.paymentQ = paymentQ;
        this.paymentDLQ = paymentDLQ;
        this.paymentQExchange = paymentQ + "_EXCHANGE";
        this.paymentDLQExchange = paymentDLQ + "_EXCHANGE";
        this.paymentQKey = paymentQ + "_KEY";
        this.paymentDLQKey = paymentDLQ + "_KEY";
    }

    @Bean
    DirectExchange paymentDLQExchange() {
        return new DirectExchange(paymentDLQExchange);
    }

    @Bean
    DirectExchange paymentQExchange() {
        return new DirectExchange(paymentQExchange);
    }

    @Bean
    Queue paymentDLQ() {
        return QueueBuilder.durable(paymentDLQ).build();
    }

    @Bean
    Queue paymentQ() {
        return QueueBuilder.durable(paymentQ)
                .withArgument("x-dead-letter-exchange", paymentDLQExchange)
                .withArgument("x-dead-letter-routing-key", paymentDLQKey)
                .build();
    }

    @Bean
    Binding publisherDLQBinding() {
        return BindingBuilder.bind(paymentDLQ())
                .to(paymentDLQExchange()).with(paymentDLQKey);
    }

    @Bean
    Binding publisherQBinding() {
        return BindingBuilder.bind(paymentQ())
                .to(paymentQExchange()).with(paymentQKey);
    }

}
