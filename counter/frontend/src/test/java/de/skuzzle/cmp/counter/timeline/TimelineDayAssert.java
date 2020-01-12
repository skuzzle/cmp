package de.skuzzle.cmp.counter.timeline;

import java.time.LocalDateTime;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import de.skuzzle.cmp.counter.timeline.TimelineDay;

public class TimelineDayAssert extends AbstractAssert<TimelineDayAssert, TimelineDay> {

    private TimelineDayAssert(TimelineDay actual) {
        super(actual, TimelineDayAssert.class);
    }

    public static TimelineDayAssert assertThat(TimelineDay actual) {
        return new TimelineDayAssert(actual);
    }

    public TimelineDayAssert hasName(String expected) {
        Assertions.assertThat(actual.getName()).isEqualTo(expected);
        return this;
    }

    public TimelineDayAssert hasTotalCount(int totalCount) {
        Assertions.assertThat(actual.getTotalCount()).isEqualTo(totalCount);
        Assertions.assertThat(actual.getIncrements()).hasSize(totalCount);
        return this;
    }

    public TimelineDayAssert hasDateUTC(LocalDateTime expected) {
        Assertions.assertThat(actual.getDateUTC()).isEqualTo(expected);
        return this;
    }
}
