package de.skuzzle.cmp.counter.domain;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.springframework.data.auditing.DateTimeProvider;

enum UTCDateTimeProvider implements DateTimeProvider {
    INSTANCE;

    static UTCDateTimeProvider getInstance() {
        return INSTANCE;
    }

    public LocalDateTime getNowLocal() {
        return getNow()
                .map(LocalDateTime::from)
                .orElseThrow();
    }

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
    }

}
