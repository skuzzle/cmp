package de.skuzzle.cmp.counter.client;

public class RestShareDefinition {

    private final String shareId;
    private final RestShareInformation shareInformation;

    RestShareDefinition(String shareId, RestShareInformation shareInformation) {
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
