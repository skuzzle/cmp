package de.skuzzle.cmp.counter.timeline;

import java.time.Month;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import de.skuzzle.cmp.counter.timeline.TimelineMonth;

public class TimelineMonthAssert extends AbstractAssert<TimelineMonthAssert, TimelineMonth> {

    private TimelineMonthAssert(TimelineMonth actual) {
        super(actual, TimelineMonthAssert.class);
    }

    public static TimelineMonthAssert assertThat(TimelineMonth actual) {
        return new TimelineMonthAssert(actual);
    }

    public TimelineMonthAssert hasName(String expected) {
        Assertions.assertThat(actual.getName()).isEqualTo(expected);
        return this;
    }

    public TimelineMonthAssert hasTotalCount(int totalCount) {
        Assertions.assertThat(actual.getTotalCount()).isEqualTo(totalCount);
        return this;
    }

    public TimelineMonthAssert isMonth(Month expected) {
        Assertions.assertThat(actual.getYearMonth().getMonth()).isEqualTo(expected);
        return this;
    }

    public TimelineDayAssert onlyDay() {
        Assertions.assertThat(actual.getDays()).hasSize(1);
        return TimelineDayAssert.assertThat(actual.getDays().get(0));
    }

    public TimelineDayAssert day(int dayOfMonth) {
        return TimelineDayAssert.assertThat(actual.getDay(dayOfMonth));
    }
}
