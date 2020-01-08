package de.skuzzle.cmp.counter.tallypage;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class TimelineMonth {

    private final String name;
    private final YearMonth yearMonth;
    private final List<TimelineIncrement> increments;

    TimelineMonth(String name, YearMonth yearMonth, List<TimelineIncrement> increments) {
        this.name = name;
        this.yearMonth = yearMonth;
        this.increments = increments;
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

    public List<TimelineIncrement> getIncrements() {
        return this.increments;
    }

    public int getTotalCount() {
        return increments.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(yearMonth);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TimelineMonth
                && Objects.equals(yearMonth, ((TimelineMonth) obj).yearMonth);
    }
}
