package de.skuzzle.tally.frontend.client;

import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TallySheet {

    private String id;
    private int version;

    @NotEmpty
    private String name;
    @NotEmpty
    private String adminKey;
    @NotEmpty
    private String publicKey;
    @NotNull
    private List<TallyIncrement> increments;
    private LocalDateTime createDateUTC;
    private LocalDateTime lastModifiedDateUTC;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
                .sorted(Comparator.comparing(TallyIncrement::getCreateDateUTC).reversed())
                .limit(max)
                .collect(Collectors.toList());
    }
}
