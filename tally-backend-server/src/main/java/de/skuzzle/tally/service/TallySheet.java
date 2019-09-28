package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Preconditions;

@Document
public class TallySheet {

    @Id
    private String id;
    @Version
    private int version;

    private final String name;
    @Indexed
    private String adminKey;
    @Indexed
    private final String publicKey;
    private final List<TallyIncrement> increments;

    // dates in UTC+0
    @CreatedDate
    private LocalDateTime createDateUTC;
    @LastModifiedDate
    private LocalDateTime lastModifiedDateUTC;

    TallySheet(String name, String adminKey, String publicKey, List<TallyIncrement> increments) {
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");
        Preconditions.checkArgument(increments != null, "increments must not be null");
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.increments = increments;
    }

    public static TallySheet newTallySheet(String name, String adminKey, String publicKey) {
        return new TallySheet(name, adminKey, publicKey, new ArrayList<>());
    }

    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public String getName() {
        return this.name;
    }

    public Optional<String> getAdminKey() {
        return Optional.ofNullable(this.adminKey);
    }

    public TallySheet withWipedAdminKey() {
        this.adminKey = null;
        return this;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public List<TallyIncrement> getIncrements() {
        return Collections.unmodifiableList(this.increments);
    }

    public IncrementQueryResult selectIncrements(IncrementQuery query) {
        Preconditions.checkArgument(query != null, "query must not be null");
        return query.select(getIncrements());
    }

    public LocalDateTime getLastModifiedDateUTC() {
        return this.lastModifiedDateUTC;
    }

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }

    public boolean deleteIncrementWithId(String incrementId) {
        Preconditions.checkArgument(incrementId != null, "incrementId must not be null");
        return this.increments.removeIf(increment -> increment.getId().equals(incrementId));
    }

    public void incrementWith(TallyIncrement increment) {
        Preconditions.checkArgument(increment != null, "increment must not be null");
        final boolean idExists = increments.stream().anyMatch(existing -> existing.getId().equals(increment.getId()));
        Preconditions.checkArgument(!idExists, "Increment with id %s already exists in tally sheet with id %s",
                increment.getId(), getId());

        this.increments.add(increment);
    }
}
