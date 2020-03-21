package de.skuzzle.cmp.counter.client;

import java.util.List;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestIncrements increments;
    private final List<RestShareDefinition> shareDefinitions;

    RestTallyResponse(RestTallySheet tallySheet, RestIncrements increments,
            List<RestShareDefinition> shareDefinitions) {
        this.tallySheet = tallySheet;
        this.increments = increments;
        this.shareDefinitions = shareDefinitions;
    }

    public RestTallySheet getTallySheet() {
        return this.tallySheet;
    }

    public RestIncrements getIncrements() {
        return this.increments;
    }

    public List<RestShareDefinition> getShareDefinitions() {
        return this.shareDefinitions;
    }
}
