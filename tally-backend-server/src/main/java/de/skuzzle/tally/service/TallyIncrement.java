package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class TallyIncrement {

    @NotNull
    private Set<String> tags;

    @NotNull
    private String description;

    @JsonFormat(pattern = TallySheet.DATE_FORMAT)
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

    void setCreateDateUTC(LocalDateTime createDateUTC) {
        this.createDateUTC = createDateUTC;
    }

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }
}
