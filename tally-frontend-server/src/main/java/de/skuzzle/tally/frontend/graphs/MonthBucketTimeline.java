package de.skuzzle.tally.frontend.graphs;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public class MonthBucketTimeline implements Timeline {

    private LocalDateTime min = LocalDateTime.now();
    private LocalDateTime max = LocalDateTime.now();
    private int[] buckets;

    private MonthBucketTimeline() {
        // hidden
    }

    public static MonthBucketTimeline initializeFrom(Stream<LocalDateTime> instants) {
        final MonthBucketTimeline result = new MonthBucketTimeline();
        instants.forEach(result::extend);
        final long months = 12 + (result.max.getYear() - result.min.getYear()) * 12;
        result.buckets = new int[Ints.saturatedCast(months)];
        return result;
    }

    private MonthBucketTimeline extend(LocalDateTime date) {
        if (date.compareTo(min) < 0) {
            min = date;
        } else if (date.compareTo(max) > 0) {
            max = date;
        }
        return this;
    }

    @Override
    public Point classify(LocalDateTime instant) {
        Preconditions.checkArgument(instant.compareTo(min) >= 0 && instant.compareTo(max) <= 0,
                "Given date %s should be >= %s and <= %s", instant, min, max);

        final int yearModifier = instant.getYear() - min.getYear();
        final int bucket = instant.getMonth().getValue() + 12 * yearModifier;
        final int y = ++buckets[bucket];
        return new Point(bucket, y);
    }

}
