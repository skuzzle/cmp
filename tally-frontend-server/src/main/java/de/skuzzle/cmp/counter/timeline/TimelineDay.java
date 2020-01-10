package de.skuzzle.cmp.counter.timeline;

import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.List;

public class TimelineDay {

    private final String name;
    private final MonthDay monthDay;
    private final LocalDateTime date;
    private final List<TimelineIncrement> increments;

    TimelineDay(String name, LocalDateTime date, List<TimelineIncrement> increments) {
        this.name = name;
        this.date = date;
        this.monthDay = MonthDay.from(date);
        this.increments = increments;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public MonthDay getMonthDay() {
        return this.monthDay;
    }

    public List<TimelineIncrement> getIncrements() {
        return this.increments;
    }

    public int getTotalCount() {
        return increments.size();
    }

}
