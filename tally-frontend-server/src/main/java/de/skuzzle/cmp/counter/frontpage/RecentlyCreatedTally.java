package de.skuzzle.cmp.counter.frontpage;

import java.time.LocalDateTime;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.counter.client.RestTallySheet;

public class RecentlyCreatedTally {

    private final LocalDateTime creationDateUTC;
    private final String name;
    private final String adminKey;
    private final String publicKey;
    private final int totalCount;

    private RecentlyCreatedTally(LocalDateTime creationDateUTC, String name, String adminKey, String publicKey,
            int totalCount) {
        this.creationDateUTC = creationDateUTC;
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.totalCount = totalCount;
    }

    public static RecentlyCreatedTally fromRestResponse(RestTallySheet response) {
        Preconditions.checkArgument(response != null, "response must not be null");
        return new RecentlyCreatedTally(response.getCreateDateUTC(), response.getName(), response.getAdminKey(),
                response.getPublicKey(), response.getTotalCount());
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

    public int getTotalCount() {
        return this.totalCount;
    }

}
