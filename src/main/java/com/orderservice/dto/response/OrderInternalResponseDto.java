package com.orderservice.dto.response;

import com.orderservice.dto.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class OrderInternalResponseDto {
    private Long userId;
    private OrderStatus status;
}
