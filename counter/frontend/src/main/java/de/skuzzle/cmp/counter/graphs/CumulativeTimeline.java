package de.skuzzle.cmp.counter.graphs;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import com.google.common.base.Preconditions;

public class CumulativeTimeline implements Timeline {

    private final TimeRange range;
    private final double length;
    private int count;

    public CumulativeTimeline(Collection<LocalDateTime> instants) {
        this.range = TimeRange.from(instants);
        this.length = range.length().toMillis();
    }

    @Override
    public Point classify(LocalDateTime instant) {
        Preconditions.checkArgument(range.contains(instant),
                "Given date %s should be contained in %s", instant, range);

        final Duration untilDate = Duration.between(range.min(), instant);
        final double x = untilDate.toMillis() / length;
        final double y = ++count;
        return new Point(x, y);
    }

}
