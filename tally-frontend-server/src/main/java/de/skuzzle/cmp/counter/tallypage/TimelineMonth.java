package de.skuzzle.cmp.counter.tallypage;

import java.time.YearMonth;
import java.util.List;

public class TimelineMonth {

    private final String name;
    private final YearMonth yearMonth;
    private final List<TimelineIncrement> increments;

    public TimelineMonth(String name, YearMonth yearMonth, List<TimelineIncrement> increments) {
        this.name = name;
        this.yearMonth = yearMonth;
        this.increments = increments;
    }

    public String getName() {
        return this.name;
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
}
