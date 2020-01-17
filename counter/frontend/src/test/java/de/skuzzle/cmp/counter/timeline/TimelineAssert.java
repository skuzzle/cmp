package de.skuzzle.cmp.counter.timeline;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class TimelineAssert extends AbstractAssert<TimelineAssert, Timeline> {

    private TimelineAssert(Timeline actual) {
        super(actual, TimelineAssert.class);
    }

    public static TimelineAssert assertThat(Timeline actual) {
        return new TimelineAssert(actual);
    }

    public TimelineAssert isAdmin(String expectedAdminKey) {
        Assertions.assertThat(actual.isAdmin()).isTrue();
        Assertions.assertThat(actual.getAdminKey()).isEqualTo(expectedAdminKey);
        return this;
    }

    public TimelineAssert isReadOnly() {
        Assertions.assertThat(actual.isAdmin()).isFalse();
        return this;
    }

    public TimelineYearAssert onlyYear() {
        Assertions.assertThat(actual.getYears()).hasSize(1);
        return TimelineYearAssert.assertThat(actual.getYears().get(0));
    }

    public TimelineYearAssert year(int year) {
        return TimelineYearAssert.assertThat(actual.year(year));
    }

}
