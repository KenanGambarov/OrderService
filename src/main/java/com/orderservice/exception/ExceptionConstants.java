package com.orderservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionConstants {

    VALIDATION_FAILED("Validation Failed"),
    UNEXPECTED_ERROR("Unexpected Error"),
    CLIENT_ERROR("Exception from Client"),
    ORDER_NOT_FOUND("Order Not Found or Not Permitted"),
    ORDER_STATUS("This order already %sED");
//    ORDER_CANCELED_OR_DELIVERED("Order Canceled or Delivered");

    private final String message;

    ExceptionConstants(String message) {
        this.message = message;
    }

    public String getMessagePattern(Object... args) {
        return String.format(this.message, args);
    }
}
