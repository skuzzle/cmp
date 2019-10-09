package de.skuzzle.tally.frontend.frontpage;

import java.time.LocalDateTime;

import com.google.common.base.Preconditions;

import de.skuzzle.tally.frontend.client.RestTallySheet;

public class RecentlyCreatedTally {

    private final LocalDateTime creationDateUTC;
    private final String name;
    private final String adminKey;
    private final String publicKey;

    private RecentlyCreatedTally(LocalDateTime creationDateUTC, String name, String adminKey, String publicKey) {
        this.creationDateUTC = creationDateUTC;
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
    }

    public static RecentlyCreatedTally fromRestResponse(RestTallySheet response) {
        Preconditions.checkArgument(response != null, "response must not be null");
        return new RecentlyCreatedTally(response.getCreateDateUTC(), response.getName(), response.getAdminKey(),
                response.getPublicKey());
    }

    public LocalDateTime getCreationDateUTC() {
        return this.creationDateUTC;
    }

    public String getName() {
        return this.name;
    }

    public String getAdminKey() {
        return this.adminKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

}
