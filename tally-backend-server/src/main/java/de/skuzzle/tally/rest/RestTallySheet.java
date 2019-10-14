package de.skuzzle.tally.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import de.skuzzle.tally.service.ShallowTallySheet;
import de.skuzzle.tally.service.UserId;

public class RestTallySheet {

    private final String name;
    private final String adminKey;
    private final String publicKey;
    private final boolean assignableToCurrentUser;

    // dates in UTC+0
    private final LocalDateTime createDateUTC;
    private final LocalDateTime lastModifiedDateUTC;

    private RestTallySheet(String name, String adminKey, String publicKey,
            LocalDateTime createDateUTC, LocalDateTime lastModifiedDateUTC, boolean assignableToCurrentUser) {
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.createDateUTC = createDateUTC;
        this.lastModifiedDateUTC = lastModifiedDateUTC;
        this.assignableToCurrentUser = assignableToCurrentUser;
    }

    public static List<RestTallySheet> fromDomainObjects(UserId currentUser,
            List<? extends ShallowTallySheet> tallySheets) {
        Preconditions.checkArgument(tallySheets != null, "tallySheets must not be null");
        return tallySheets.stream()
                .map(tallySheet -> fromDomainObject(currentUser, tallySheet))
                .collect(Collectors.toList());
    }

    public static RestTallySheet fromDomainObject(UserId currentUser, ShallowTallySheet tallySheet) {
        Preconditions.checkArgument(currentUser != null, "currentUser must not be null");
        Preconditions.checkArgument(tallySheet != null, "tallySheet must not be null");
        return new RestTallySheet(tallySheet.getName(),
                tallySheet.getAdminKey().orElse(null),
                tallySheet.getPublicKey(),
                tallySheet.getCreateDateUTC(),
                tallySheet.getLastModifiedDateUTC(),
                tallySheet.isAssignableTo(currentUser));
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

    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }

    public LocalDateTime getLastModifiedDateUTC() {
        return this.lastModifiedDateUTC;
    }

    public boolean isAssignableToCurrentUser() {
        return this.assignableToCurrentUser;
    }

}
