package de.skuzzle.tally.frontend.client;

import java.time.LocalDateTime;
import java.util.Set;

public class TallyIncrement {

    private Set<String> tags;
    private String description;
    private LocalDateTime incrementDateUTC;

    public TallyIncrement() {
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getIncrementDateUTC() {
        return incrementDateUTC;
    }

    public void setIncrementDateUTC(LocalDateTime incrementDateUTC) {
        this.incrementDateUTC = incrementDateUTC;
    }
}
