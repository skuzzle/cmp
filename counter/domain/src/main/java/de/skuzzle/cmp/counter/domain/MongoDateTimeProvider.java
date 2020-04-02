package de.skuzzle.cmp.counter.domain;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.springframework.data.auditing.DateTimeProvider;

import de.skuzzle.cmp.common.time.UTCDateTimeProvider;

class MongoDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return UTCDateTimeProvider.getInstance().getNow();
    }

}
