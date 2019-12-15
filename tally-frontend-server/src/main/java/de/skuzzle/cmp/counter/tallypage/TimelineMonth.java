package de.skuzzle.cmp.counter.tallypage;

import java.time.Month;
import java.util.List;

public class TimelineMonth {

    private final String name;
    private final Month month;
    private final List<TimelineIncrement> increments;

    public TimelineMonth(String name, Month month, List<TimelineIncrement> increments) {
        this.name = name;
        this.month = month;
        this.increments = increments;
    }

    public String getName() {
        return this.name;
    }

    public Month getMonth() {
        return this.month;
    }

    public List<TimelineIncrement> getIncrements() {
        return this.increments;
    }

    public int getTotalCount() {
        return increments.size();
    }
}
