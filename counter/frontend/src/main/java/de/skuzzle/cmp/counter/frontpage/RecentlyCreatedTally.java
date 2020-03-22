package de.skuzzle.cmp.counter.frontpage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.counter.client.RestTallySheet;

public class RecentlyCreatedTally {

    private final LocalDateTime lastModifiedDateUTC;
    private final LocalDateTime creationDateUTC;
    private final String name;
    private final String adminKey;
    private final String publicKey;
    private final int totalCount;

    private RecentlyCreatedTally(LocalDateTime lastModifiedDateUTC, LocalDateTime creationDateUTC, String name,
            String adminKey, String publicKey,
            int totalCount) {
        this.lastModifiedDateUTC = lastModifiedDateUTC;
        this.creationDateUTC = creationDateUTC;
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.totalCount = totalCount;
    }

    public static RecentlyCreatedTally fromRestResponse(RestTallySheet response) {
        Preconditions.checkArgument(response != null, "response must not be null");
        return new RecentlyCreatedTally(response.getLastModifiedDateUTC(),
                response.getCreateDateUTC(),
                response.getName(),
                response.getAdminKey(),
                response.getDefaultShareDefinition().getShareId(),
                response.getTotalCount());
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

    public String getLastModifiedInfo() {
        final LocalDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime();
        final Duration durationSinceModification = Duration.between(lastModifiedDateUTC, nowUtc);
        return ApproximateDuration.fromExactDuration(durationSinceModification).toString();
    }

}
