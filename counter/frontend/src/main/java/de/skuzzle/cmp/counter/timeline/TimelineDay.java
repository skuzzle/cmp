package de.skuzzle.cmp.counter.timeline;

import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.List;

public class TimelineDay {

    private final String name;
    private final MonthDay monthDay;
    private final LocalDateTime dateUTC;
    private final List<TimelineIncrement> increments;

    TimelineDay(String name, LocalDateTime dateUTC, List<TimelineIncrement> increments) {
        this.name = name;
        this.dateUTC = dateUTC;
        this.monthDay = MonthDay.from(dateUTC);
        this.increments = increments;
    }

    public LocalDateTime getDateUTC() {
        return this.dateUTC;
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
