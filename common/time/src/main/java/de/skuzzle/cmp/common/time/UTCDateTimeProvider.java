package de.skuzzle.cmp.common.time;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public enum UTCDateTimeProvider {
    INSTANCE;

    public static UTCDateTimeProvider getInstance() {
        return INSTANCE;
    }

    public LocalDateTime getNowLocal() {
        return getNow()
                .map(LocalDateTime::from)
                .orElseThrow();
    }

    public Optional<TemporalAccessor> getNow() {
        return Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
    }

}
