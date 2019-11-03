package de.skuzzle.cmp.frontend.graphs;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class TimeRange {

    private final LocalDateTime min;
    private final LocalDateTime max;

    private TimeRange(LocalDateTime min, LocalDateTime max) {
        this.min = min;
        this.max = max;
    }

    public static <T> TimeRange extractFrom(Iterable<T> elements, Function<? super T, LocalDateTime> extractor) {
        final Iterable<LocalDateTime> instants = StreamSupport.stream(elements.spliterator(), false)
                .map(extractor)::iterator;
        return from(instants);
    }

    public static TimeRange from(Iterable<LocalDateTime> instants) {
        final Iterator<LocalDateTime> iterator = instants.iterator();
        LocalDateTime min = LocalDateTime.now();
        LocalDateTime max = LocalDateTime.now();
        while (iterator.hasNext()) {
            final LocalDateTime instant = iterator.next();
            if (instant.compareTo(min) < 0) {
                min = instant;
            } else if (instant.compareTo(max) > 0) {
                max = instant;
            }
        }
        return new TimeRange(min, max);
    }

    public boolean isSingleMonth() {
        return min.getMonth().equals(max.getMonth());
    }

    public Duration length() {
        return Duration.between(min, max);
    }

    public LocalDateTime min() {
        return min;
    }

    public LocalDateTime max() {
        return max;
    }

    public boolean contains(LocalDateTime instant) {
        return instant.compareTo(min) >= 0 && instant.compareTo(max) <= 0;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", min.toString(), max.toString());
    }

}
