package com.orderservice.util.constraints;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum OrderCacheDurationConstraints {
    MINUTE(1L,ChronoUnit.MINUTES),
    HOUR(1L, ChronoUnit.HOURS),
    DAY(1L, ChronoUnit.DAYS),
    MONTH(1L, ChronoUnit.MONTHS);

    private final Long duration;
    private final TemporalUnit unit;

    OrderCacheDurationConstraints(Long duration, TemporalUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }

    public Duration toDuration() {
        return Duration.of(duration, unit);
    }
}
