package de.skuzzle.cmp.counter.timeline;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.primitives.Ints;

public class TimelineMonth {

    private final String name;
    private final YearMonth yearMonth;
    private final List<TimelineDay> days;
    private final int totalCount;

    TimelineMonth(String name, YearMonth yearMonth, List<TimelineDay> days) {
        this.name = name;
        this.yearMonth = yearMonth;
        this.days = days;

        final long sum = days.stream()
                .collect(Collectors.summarizingInt(TimelineDay::getTotalCount))
                .getSum();
        this.totalCount = Ints.saturatedCast(sum);
    }

    public String getName() {
        return this.name;
    }

    public Year getYear() {
        return Year.from(yearMonth);
    }

    public YearMonth getYearMonth() {
        return this.yearMonth;
    }

    public TimelineDay getDay(int dayOfMonth) {
        return this.days.stream()
                .filter(tlDay -> tlDay.getMonthDay().getDayOfMonth() == dayOfMonth)
                .findFirst()
                .orElseThrow();
    }

    public List<TimelineDay> getDays() {
        return this.days;
    }

    public int getTotalCount() {
        return this.totalCount;
    }
}
