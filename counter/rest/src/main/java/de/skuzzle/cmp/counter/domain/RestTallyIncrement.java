package de.skuzzle.cmp.counter.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class RestTallyIncrement {

    private final String id;
    private final String description;
    private final Set<String> tags;
    private final LocalDateTime incrementDateUTC;

    public RestTallyIncrement(String id, String description, Collection<String> tags, LocalDateTime incrementDateUTC) {
        this.id = id;
        this.description = description;
        this.tags = Set.copyOf(tags);
        this.incrementDateUTC = incrementDateUTC;
    }

    public static List<RestTallyIncrement> fromDomainObjects(List<TallyIncrement> increments) {
        return increments.stream()
                .map(RestTallyIncrement::fromDomainObject)
                .collect(Collectors.toList());
    }

    public static RestTallyIncrement fromDomainObject(TallyIncrement tallyIncrement) {
        Preconditions.checkArgument(tallyIncrement != null, "tallyIncrement must not be null");
        return new RestTallyIncrement(
                tallyIncrement.getId(),
                tallyIncrement.getDescription(),
                tallyIncrement.getTags(),
                tallyIncrement.getIncrementDateUTC());
    }

    public TallyIncrement toDomainObjectWithoutId() {
        return TallyIncrement.newIncrement(description, incrementDateUTC, tags);
    }

    public TallyIncrement toDomainObjectWithId() {
        return TallyIncrement.newIncrementWithId(id, description, incrementDateUTC, tags);
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
