package de.skuzzle.cmp.counter.tallypage;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.primitives.Ints;

public class TimelineMonth {

    private final String name;
    private final YearMonth yearMonth;
    private final List<TimelineDay> days;

    TimelineMonth(String name, YearMonth yearMonth, List<TimelineDay> days) {
        this.name = name;
        this.yearMonth = yearMonth;
        this.days = days;
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

    public List<TimelineDay> getDays() {
        return this.days;
    }

    public int getTotalCount() {
        final long sum = days.stream()
                .collect(Collectors.summarizingInt(TimelineDay::getTotalCount))
                .getSum();
        return Ints.saturatedCast(sum);
    }
}
