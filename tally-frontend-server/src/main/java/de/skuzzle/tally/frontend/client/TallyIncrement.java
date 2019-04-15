package de.skuzzle.tally.frontend.client;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class TallyIncrement {

    @NotNull
    private Set<String> tags;

    @NotNull
    private String description;
    private LocalDateTime createDateUTC;

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

    public void setCreateDateUTC(LocalDateTime createDateUTC) {
        this.createDateUTC = createDateUTC;
    }

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }
}
