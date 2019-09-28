package de.skuzzle.tally.frontend.client;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TallySheet {

    private String name;
    private String adminKey;
    private String publicKey;
    private List<TallyIncrement> increments;
    private LocalDateTime createDateUTC;
    private LocalDateTime lastModifiedDateUTC;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminKey() {
        return this.adminKey;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<TallyIncrement> getIncrements() {
        return this.increments;
    }

    public void setIncrements(List<TallyIncrement> increments) {
        this.increments = increments;
    }

    public LocalDateTime getLastModifiedDateUTC() {
        return this.lastModifiedDateUTC;
    }

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }

    public boolean isAdmin() {
        return adminKey != null;
    }

    public boolean hasIncrements() {
        return !increments.isEmpty();
    }

    public List<TallyIncrement> getHistory(int max) {
        return getIncrements().stream()
                .sorted(Comparator.comparing(TallyIncrement::getIncrementDateUTC).reversed())
                .limit(max)
                .collect(Collectors.toList());
    }
}
