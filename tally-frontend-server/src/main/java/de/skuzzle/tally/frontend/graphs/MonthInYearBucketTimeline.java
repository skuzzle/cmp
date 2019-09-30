package de.skuzzle.tally.frontend.graphs;

import java.time.LocalDateTime;
import java.util.Collection;

import com.google.common.base.Preconditions;

public class MonthInYearBucketTimeline implements Timeline {

    private final TimeRange range;
    private final int[] buckets;

    public MonthInYearBucketTimeline(Collection<LocalDateTime> instants) {
        this.range = TimeRange.from(instants);

        final int bucketCount = 12 + (range.max().getYear() - range.min().getYear()) * 12;
        buckets = new int[bucketCount];
        instants.forEach(this::fillBucket);
    }

    private void fillBucket(LocalDateTime instant) {
        final int yearModifier = instant.getYear() - range.min().getYear();
        final int bucket = (instant.getMonth().getValue() - 1) + 12 * yearModifier;
        ++buckets[bucket];
    }

    @Override
    public Point classify(LocalDateTime instant) {
        Preconditions.checkArgument(range.contains(instant),
                "Given date %s should be contained in %s", instant, range);

        final int yearModifier = instant.getYear() - range.min().getYear();
        final int bucket = (instant.getMonth().getValue() - 1) + 12 * yearModifier;
        final int y = buckets[bucket];
        return new Point(bucket, y);
    }

}
