package de.skuzzle.cmp.counter.client;

import com.google.common.base.Preconditions;

public class RestShareDefinition {

    private final String shareId;
    private final RestShareInformation shareInformation;

    RestShareDefinition(String shareId, RestShareInformation shareInformation) {
        Preconditions.checkArgument(shareId != null, "shareId must not be null");
        Preconditions.checkArgument(shareInformation != null, "shareInformation must be null");
        this.shareId = shareId;
        this.shareInformation = shareInformation;
    }

    public String getShareId() {
        return this.shareId;
    }

    public RestShareInformation getShareInformation() {
        return this.shareInformation;
    }
}
