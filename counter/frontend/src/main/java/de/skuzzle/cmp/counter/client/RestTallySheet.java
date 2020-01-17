package de.skuzzle.cmp.counter.client;

import java.time.LocalDateTime;

public class RestTallySheet {

    private final String name;
    private final String adminKey;
    private final String publicKey;

    // dates in UTC+0
    private final LocalDateTime createDateUTC;
    private final LocalDateTime lastModifiedDateUTC;
    private final boolean assignableToCurrentUser;
    private final int totalCount;

    RestTallySheet(String name, String adminKey, String publicKey,
            LocalDateTime createDateUTC, LocalDateTime lastModifiedDateUTC, boolean assignableToCurrentUser,
            int totalCount) {
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.createDateUTC = createDateUTC;
        this.lastModifiedDateUTC = lastModifiedDateUTC;
        this.assignableToCurrentUser = assignableToCurrentUser;
        this.totalCount = totalCount;
    }

    public String getName() {
        return this.name;
    }

    public String getAdminKey() {
        return this.adminKey;
    }

    public boolean isAdmin() {
        return adminKey != null;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }

    public LocalDateTime getLastModifiedDateUTC() {
        return this.lastModifiedDateUTC;
    }

    public boolean isAssignableToCurrentUser() {
        return this.assignableToCurrentUser;
    }

    public int getTotalCount() {
        return this.totalCount;
    }
}
