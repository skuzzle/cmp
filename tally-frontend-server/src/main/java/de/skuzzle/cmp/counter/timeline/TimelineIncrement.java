package de.skuzzle.cmp.counter.timeline;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Set;

import de.skuzzle.cmp.counter.client.RestTallyIncrement;

public class TimelineIncrement {

    private final String id;
    private final LocalDateTime incrementDateUTC;
    private final String description;
    private final Set<String> tags;

    private TimelineIncrement(String id, LocalDateTime incrementDateUTC, String description, Set<String> tags) {
        this.id = id;
        this.incrementDateUTC = incrementDateUTC;
        this.description = description;
        this.tags = tags;
    }

    static TimelineIncrement fromBackendResponse(RestTallyIncrement increment) {
        return new TimelineIncrement(increment.getId(),
                increment.getIncrementDateUTC(),
                increment.getDescription(),
                increment.getTags().all());
    }

    public String getId() {
        return this.id;
    }

    Month getMonth() {
        return incrementDateUTC.getMonth();
    }

    YearMonth getYearMonth() {
        return YearMonth.from(incrementDateUTC);
    }

    public LocalDateTime getIncrementDateUTC() {
        return this.incrementDateUTC;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return !description.isEmpty();
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public boolean isEmpty() {
        return tags.isEmpty() && description.isEmpty();
    }

    public boolean hasTags() {
        return !tags.isEmpty();
    }

}
