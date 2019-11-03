package de.skuzzle.cmp.frontend.graphs;

import java.time.LocalDateTime;
import java.util.Collection;

import com.google.common.base.Preconditions;

public class DayInMonthBucketTimeline implements Timeline {

    private final TimeRange range;
    private final int[] buckets;

    public DayInMonthBucketTimeline(Collection<LocalDateTime> instants) {
        this.range = TimeRange.from(instants);

        final int bucketCount = range.min().getMonth().maxLength();
        buckets = new int[bucketCount];
        instants.forEach(this::fillBucket);
    }

    private void fillBucket(LocalDateTime instant) {
        final int bucket = instant.getDayOfMonth() - 1;
        ++buckets[bucket];
    }

    @Override
    public Point classify(LocalDateTime instant) {
        Preconditions.checkArgument(range.contains(instant),
                "Given date %s should be contained in %s", instant, range);

        final int bucket = instant.getDayOfMonth() - 1;
        final int y = buckets[bucket];
        return new Point(bucket, y);
    }

}
