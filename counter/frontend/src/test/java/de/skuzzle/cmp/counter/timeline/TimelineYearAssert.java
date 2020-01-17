package de.skuzzle.cmp.counter.timeline;

import java.time.Month;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class TimelineYearAssert extends AbstractAssert<TimelineYearAssert, TimelineYear> {

    private TimelineYearAssert(TimelineYear actual) {
        super(actual, TimelineYearAssert.class);
    }

    public static TimelineYearAssert assertThat(TimelineYear year) {
        return new TimelineYearAssert(year);
    }

    public TimelineYearAssert hasName(String expected) {
        Assertions.assertThat(actual.getName()).isEqualTo(expected);
        return this;
    }

    public TimelineYearAssert isYear(int year) {
        Assertions.assertThat(actual.getYear()).isEqualTo(year);
        return this;
    }

    public TimelineYearAssert hasTotalCount(int totalCount) {
        Assertions.assertThat(actual.getTotalCount()).isEqualTo(totalCount);
        return this;
    }

    public TimelineMonthAssert onlyMonth() {
        Assertions.assertThat(actual.getMonths()).hasSize(1);
        return TimelineMonthAssert.assertThat(actual.getMonths().get(0));
    }

    public TimelineMonthAssert month(Month month) {
        return TimelineMonthAssert.assertThat(actual.getMonth(month));
    }

}
