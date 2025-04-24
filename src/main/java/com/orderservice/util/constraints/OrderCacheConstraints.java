package com.orderservice.util.constraints;

public enum OrderCacheConstraints {

    ORDER_KEY("ms-order:order:%s"),
    ORDER_ITEMS_KEY("ms-order:order-items:%s");

    private final String keyFormat;

    OrderCacheConstraints(String keyFormat) {
        this.keyFormat = keyFormat;
    }

    public String getKey(Object... args) {
        return String.format(this.keyFormat, args);
    }
}
