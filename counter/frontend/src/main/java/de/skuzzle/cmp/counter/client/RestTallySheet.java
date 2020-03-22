package de.skuzzle.cmp.counter.client;

import java.time.LocalDateTime;
import java.util.List;

import com.google.common.base.Preconditions;

public class RestTallySheet {

    private final String name;
    private final String adminKey;

    // dates in UTC+0
    private final LocalDateTime createDateUTC;
    private final LocalDateTime lastModifiedDateUTC;
    private final boolean assignableToCurrentUser;
    private final int totalCount;
    private final List<RestShareDefinition> shareDefinitions;

    RestTallySheet(String name, String adminKey,
            LocalDateTime createDateUTC, LocalDateTime lastModifiedDateUTC, boolean assignableToCurrentUser,
            int totalCount, List<RestShareDefinition> shareDefinitions) {
        Preconditions.checkArgument(shareDefinitions.size() > 0,
                "first eleement in shareDefinitions should always be the defaul share, but list was empty");
        this.name = name;
        this.adminKey = adminKey;
        this.createDateUTC = createDateUTC;
        this.lastModifiedDateUTC = lastModifiedDateUTC;
        this.assignableToCurrentUser = assignableToCurrentUser;
        this.totalCount = totalCount;
        this.shareDefinitions = shareDefinitions;
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

    public RestShareDefinition getDefaultShareDefinition() {
        return shareDefinitions.get(0);
    }

    public List<RestShareDefinition> getShareDefinitions() {
        return this.shareDefinitions;
    }
}
