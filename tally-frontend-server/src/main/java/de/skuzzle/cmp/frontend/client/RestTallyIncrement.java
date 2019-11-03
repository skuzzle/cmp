package de.skuzzle.cmp.frontend.client;

import java.time.LocalDateTime;
import java.util.Set;

import com.google.common.base.Preconditions;

public class RestTallyIncrement {

    private final String id;
    private final String description;
    private final Set<String> tags;
    private final LocalDateTime incrementDateUTC;

    RestTallyIncrement(String id, String description, Set<String> tags, LocalDateTime incrementDateUTC) {
        this.id = id;
        this.description = description;
        this.tags = tags;
        this.incrementDateUTC = incrementDateUTC;
    }

    public static RestTallyIncrement createNew(String description, LocalDateTime incrementDateUTC, Set<String> tags) {
        Preconditions.checkArgument(description != null, "description must not be null");
        Preconditions.checkArgument(incrementDateUTC != null, "incrementDateUTC must not be null");
        Preconditions.checkArgument(tags != null, "tags must not be null");
        return new RestTallyIncrement(null, description, tags, incrementDateUTC);
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public LocalDateTime getIncrementDateUTC() {
        return this.incrementDateUTC;
    }

}
