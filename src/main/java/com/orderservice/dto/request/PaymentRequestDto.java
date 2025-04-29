package com.orderservice.dto.request;

import com.orderservice.dto.enums.PaymentStatus;
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
public class PaymentRequestDto {

    private Long userId;

    private Long orderId;

    private PaymentStatus status;

}
