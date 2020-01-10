package de.skuzzle.cmp.counter.tallypage;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.primitives.Ints;

public class TimelineYear {

    private final String name;
    private final Year year;
    private final List<TimelineMonth> months;
    private final int totalCount;

    TimelineYear(String name, Year year, List<TimelineMonth> months) {
        this.name = name;
        this.year = year;
        this.months = months;
        final long sum = months.stream()
                .collect(Collectors.summarizingInt(TimelineMonth::getTotalCount))
                .getSum();
        this.totalCount = Ints.saturatedCast(sum);
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

    public int getTotalCount() {
        return totalCount;
    }

}
