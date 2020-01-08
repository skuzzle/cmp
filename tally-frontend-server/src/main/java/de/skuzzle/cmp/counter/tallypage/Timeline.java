package de.skuzzle.cmp.counter.tallypage;

import java.util.List;

public class Timeline {

    private final List<TimelineYear> years;

    Timeline(List<TimelineYear> years) {
        this.years = years;
    }

    public List<TimelineYear> getYears() {
        return this.years;
    }
}
