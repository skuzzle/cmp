package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document
public class TallySheet {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Id
    private String id;
    @Version
    private int version;

    @NotEmpty
    private String name;
    @NotEmpty
    @Indexed
    private String adminKey;
    @NotEmpty
    @Indexed
    private String publicKey;
    @NotNull
    private List<TallyIncrement> increments;

    // dates in UTC+0
    @CreatedDate
    //@JsonFormat(pattern = TallySheet.DATE_FORMAT)
    private LocalDateTime createDateUTC;
    @LastModifiedDate
    //@JsonFormat(pattern = TallySheet.DATE_FORMAT)
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

    void setLastModifiedDateUTC(LocalDateTime lastModifiedDate) {
        this.lastModifiedDateUTC = lastModifiedDate;
    }

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }

    void setCreateDateUTC(LocalDateTime createDate) {
        this.createDateUTC = createDate;
    }
}
