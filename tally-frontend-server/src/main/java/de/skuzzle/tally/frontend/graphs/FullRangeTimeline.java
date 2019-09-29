package de.skuzzle.tally.frontend.graphs;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

public class FullRangeTimeline implements Timeline {

    private LocalDateTime min = LocalDateTime.now();
    private LocalDateTime max = LocalDateTime.now();
    private double length = 0;
    private int count;

    private FullRangeTimeline() {
        // hidden
    }

    public static FullRangeTimeline initializeFrom(Stream<LocalDateTime> instants) {
        final FullRangeTimeline result = new FullRangeTimeline();
        instants.forEach(result::extend);
        return result;
    }

    private FullRangeTimeline extend(LocalDateTime date) {
        if (date.compareTo(min) < 0) {
            min = date;
        } else if (date.compareTo(max) > 0) {
            max = date;
        }

        final Duration diff = Duration.between(min, max);
        this.length = diff.toMillis();
        return this;
    }

    @Override
    public Point classify(LocalDateTime instant) {
        Preconditions.checkArgument(instant.compareTo(min) >= 0 && instant.compareTo(max) <= 0,
                "Given date %s should be >= %s and <= %s", instant, min, max);

        final Duration untilDate = Duration.between(min, instant);
        final double x = untilDate.toMillis() / length;
        final double y = ++count;
        return new Point(x, y);
    }

}
