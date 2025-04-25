package com.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orderservice.dto.enums.OrderStatus;
import com.orderservice.dto.enums.PaymentStatus;
import com.orderservice.entity.OrderItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDto {

    private Long userId;

    private String shippingAddress;

    private List<OrderItemRequestDto> orderItems;

}
