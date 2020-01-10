package de.skuzzle.cmp.counter.frontpage;

import java.time.Duration;
import java.util.List;

import com.google.common.base.Preconditions;

public class ApproximateDuration {

    private static class DurationToString {
        private final Duration duration;
        private final String approximateString;

        private DurationToString(Duration duration, String approximateString) {
            this.duration = duration;
            this.approximateString = approximateString;
        }

        private boolean isLessOrEqualTo(Duration otherDuration) {
            return otherDuration.compareTo(this.duration) <= 0;
        }

        private String getApproximateString() {
            return this.approximateString;
        }
    }

    private static final List<DurationToString> APPROXIMATE_DURATIONS = List.of(
            new DurationToString(Duration.ofMinutes(2), "Just now"),
            new DurationToString(Duration.ofMinutes(10), "Few minutes ago"),
            new DurationToString(Duration.ofMinutes(59), "Less than an hour ago"),
            new DurationToString(Duration.ofHours(24), "Hours ago"),
            new DurationToString(Duration.ofDays(2), "Yesterday"),
            new DurationToString(Duration.ofDays(7), "Days ago"),
            new DurationToString(Duration.ofDays(30), "Weeks ago"),
            new DurationToString(Duration.ofSeconds(Long.MAX_VALUE), "Long ago"));

    private final Duration exactDuration;
    private final DurationToString approximation;

    private ApproximateDuration(Duration exactDuration) {
        this.exactDuration = exactDuration;
        this.approximation = findBestApproximation(exactDuration);
    }

    private DurationToString findBestApproximation(Duration exactDuration) {
        return APPROXIMATE_DURATIONS.stream()
                .filter(approximation -> approximation.isLessOrEqualTo(exactDuration))
                .findFirst()
                .orElseThrow();
    }

    public static ApproximateDuration fromExactDuration(Duration exactDuration) {
        Preconditions.checkArgument(exactDuration != null, "exactDuration must not be null");
        return new ApproximateDuration(exactDuration);
    }

    public Duration getExactDuration() {
        return this.exactDuration;
    }

    @Override
    public String toString() {
        return approximation.getApproximateString();
    }

}
