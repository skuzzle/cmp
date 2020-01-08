package de.skuzzle.cmp.counter.tallypage;

import java.time.Year;
import java.util.List;

public class TimelineYear {

    private final String name;
    private final Year year;
    private final List<TimelineMonth> months;

    TimelineYear(String name, Year year, List<TimelineMonth> months) {
        this.name = name;
        this.year = year;
        this.months = months;
    }

    public String getName() {
        return this.name;
    }

    public Year getYear() {
        return this.year;
    }

    public List<TimelineMonth> getMonths() {
        return this.months;
    }

}
