package de.skuzzle.tally.frontend.client;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class TallyIncrement {

    @NotNull
    private Set<String> tags;

    @NotNull
    private String description;
    private LocalDateTime createDate;

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

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }
}
