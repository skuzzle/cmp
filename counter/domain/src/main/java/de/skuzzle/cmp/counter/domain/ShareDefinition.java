package de.skuzzle.cmp.counter.domain;

import com.google.common.base.Preconditions;

public class ShareDefinition {

    private final String shareId;
    private final ShareInformation shareInformation;

    private ShareDefinition(String shareId, ShareInformation shareInformation) {
        Preconditions.checkArgument(shareId != null, "shareId must not be null");
        Preconditions.checkArgument(shareInformation != null, "shareInformation must not be null");
        this.shareId = shareId;
        this.shareInformation = shareInformation;
    }

    public static ShareDefinition of(String shareId, ShareInformation shareInformation) {
        return new ShareDefinition(shareId, shareInformation);
    }

    public String getShareId() {
        return this.shareId;
    }

    public ShareInformation getShareInformation() {
        return this.shareInformation;
    }
}
