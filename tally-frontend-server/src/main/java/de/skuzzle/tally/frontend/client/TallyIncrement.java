package de.skuzzle.tally.frontend.client;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TallyIncrement {

    @NotEmpty
    private String id;

    @NotNull
    private Set<String> tags;

    @NotNull
    private String description;
    private LocalDateTime createDateUTC;

    public TallyIncrement() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
