package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class TallySheet {

    @Id
    private String id;
    @Version
    private int version;

    @NotEmpty
    private String name;
    @NotEmpty
    private String adminKey;
    @NotEmpty
    private String publicKey;
    @NotNull
    private List<TallyIncrement> increments;

    // dates in UTC+0
    @CreatedDate
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime createDate;
    @LastModifiedDate
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime lastModifiedDate;

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

    public LocalDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
