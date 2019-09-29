package de.skuzzle.tally.frontend.graphs;

import java.time.LocalDateTime;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public class MonthBucketTimeline implements Timeline {

    private LocalDateTime min = LocalDateTime.now();
    private LocalDateTime max = LocalDateTime.now();
    private final int[] buckets;

    public MonthBucketTimeline(Collection<LocalDateTime> instants) {
        instants.forEach(this::extend);

        final long months = 12 + (max.getYear() - min.getYear()) * 12;
        buckets = new int[Ints.saturatedCast(months)];
        instants.forEach(this::fillBucket);
    }

    private void fillBucket(LocalDateTime instant) {
        final int yearModifier = instant.getYear() - min.getYear();
        final int bucket = instant.getMonth().getValue() + 12 * yearModifier;
        ++buckets[bucket];
    }

    private MonthBucketTimeline extend(LocalDateTime instant) {
        if (instant.compareTo(min) < 0) {
            min = instant;
        } else if (instant.compareTo(max) > 0) {
            max = instant;
        }
        return this;
    }

    @Override
    public Point classify(LocalDateTime instant) {
        Preconditions.checkArgument(instant.compareTo(min) >= 0 && instant.compareTo(max) <= 0,
                "Given date %s should be >= %s and <= %s", instant, min, max);

        final int yearModifier = instant.getYear() - min.getYear();
        final int bucket = instant.getMonth().getValue() + 12 * yearModifier;
        final int y = buckets[bucket];
        return new Point(bucket, y);
    }

}
