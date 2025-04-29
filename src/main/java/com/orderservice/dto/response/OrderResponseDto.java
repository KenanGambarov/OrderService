package com.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    private Long userId;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private Date orderDate;

    private String shippingAddress;

    private PaymentStatus paymentStatus;

    private List<OrderItemResponseDto> orderItems;

}
