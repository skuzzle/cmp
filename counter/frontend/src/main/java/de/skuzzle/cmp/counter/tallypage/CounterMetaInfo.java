package de.skuzzle.cmp.counter.tallypage;

import de.skuzzle.cmp.counter.client.RestTallyResponse;

public class CounterMetaInfo {

    private final String name;
    private final int totalCount;
    private final String adminKey;
    private final boolean assignableToCurrentUser;

    private CounterMetaInfo(String name, int totalCount, String adminKey, boolean assignableToCurrentUser) {
        this.name = name;
        this.totalCount = totalCount;
        this.adminKey = adminKey;
        this.assignableToCurrentUser = assignableToCurrentUser;
    }

    public static CounterMetaInfo fromBackendResponse(RestTallyResponse response) {
        return new CounterMetaInfo(response.getTallySheet().getName(), response.getTallySheet().getTotalCount(),
                response.getTallySheet().getAdminKey(), response.getTallySheet().isAssignableToCurrentUser());
    }

    public boolean isAdmin() {
        return this.adminKey != null;
    }

    public boolean isAssignableToCurrentUser() {
        return this.assignableToCurrentUser;
    }

    public String getName() {
        return this.name;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public String getAdminKey() {
        return this.adminKey;
    }
}
