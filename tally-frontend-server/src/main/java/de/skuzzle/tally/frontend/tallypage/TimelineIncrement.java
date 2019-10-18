package de.skuzzle.tally.frontend.tallypage;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import de.skuzzle.tally.frontend.client.RestTallyIncrement;

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
                increment.getTags());
    }

    public String getId() {
        return this.id;
    }

    Month getMonth() {
        return incrementDateUTC.getMonth();
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

    public boolean hasTags() {
        return !tags.isEmpty();
    }

}
