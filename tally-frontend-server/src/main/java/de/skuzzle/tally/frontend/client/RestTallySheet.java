package de.skuzzle.tally.frontend.client;

import java.time.LocalDateTime;

public class RestTallySheet {

    private final String name;
    private final String adminKey;
    private final String publicKey;

    // dates in UTC+0
    private final LocalDateTime createDateUTC;
    private final LocalDateTime lastModifiedDateUTC;

    RestTallySheet(String name, String adminKey, String publicKey,
            LocalDateTime createDateUTC, LocalDateTime lastModifiedDateUTC) {
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.createDateUTC = createDateUTC;
        this.lastModifiedDateUTC = lastModifiedDateUTC;
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

}
