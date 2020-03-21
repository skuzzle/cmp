package de.skuzzle.cmp.counter.domain;

import java.util.List;

import com.google.common.base.Preconditions;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestIncrements increments;
    private final List<RestShareDefinition> shareDefinitions;

    private RestTallyResponse(RestTallySheet tallySheet, RestIncrements increments,
            List<RestShareDefinition> shareDefinitions) {
        this.tallySheet = tallySheet;
        this.increments = increments;
        this.shareDefinitions = shareDefinitions;
    }

    public static RestTallyResponse of(RestTallySheet tallySheet, RestIncrements increments,
            List<RestShareDefinition> shareDefinitions) {
        Preconditions.checkArgument(tallySheet != null, "tallySheet must not be null");
        Preconditions.checkArgument(increments != null, "increments must not be null");
        Preconditions.checkArgument(shareDefinitions != null, "shareDefinitions must not be null");
        return new RestTallyResponse(tallySheet, increments, shareDefinitions);
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
