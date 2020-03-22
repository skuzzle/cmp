package de.skuzzle.cmp.counter.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class RestTallySheet {

    private final String name;
    private final String adminKey;
    private final boolean assignableToCurrentUser;
    private final int totalCount;

    // dates in UTC+0
    private final LocalDateTime createDateUTC;
    private final LocalDateTime lastModifiedDateUTC;
    private final List<RestShareDefinition> shareDefinitions;

    private RestTallySheet(String name, String adminKey,
            LocalDateTime createDateUTC, LocalDateTime lastModifiedDateUTC, boolean assignableToCurrentUser,
            int totalCount, List<RestShareDefinition> shareDefinitions) {
        this.name = name;
        this.adminKey = adminKey;
        this.createDateUTC = createDateUTC;
        this.lastModifiedDateUTC = lastModifiedDateUTC;
        this.assignableToCurrentUser = assignableToCurrentUser;
        this.totalCount = totalCount;
        this.shareDefinitions = shareDefinitions;
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
        final List<RestShareDefinition> shareDefinitions = RestShareDefinition
                .fromDomainObjects(tallySheet.getShareDefinitions());

        return new RestTallySheet(tallySheet.getName(),
                tallySheet.getAdminKey().orElse(null),
                tallySheet.getCreateDateUTC(),
                tallySheet.getLastModifiedDateUTC(),
                tallySheet.isAssignableTo(currentUser),
                tallySheet.getTotalCount(),
                shareDefinitions);
    }

    public String getName() {
        return this.name;
    }

    public String getAdminKey() {
        return this.adminKey;
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

    public List<RestShareDefinition> getShareDefinitions() {
        return this.shareDefinitions;
    }
}
