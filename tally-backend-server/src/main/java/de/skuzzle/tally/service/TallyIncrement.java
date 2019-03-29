package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class TallyIncrement {

    @NotNull
    private Set<String> tags;

    @NotNull
    private String description;

    @DateTimeFormat(iso = ISO.DATE_TIME)
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

    void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }
}
