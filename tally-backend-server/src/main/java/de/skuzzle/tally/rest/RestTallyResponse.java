package de.skuzzle.tally.rest;

import com.google.common.base.Preconditions;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestIncrements increments;

    private RestTallyResponse(RestTallySheet tallySheet, RestIncrements increments) {
        this.tallySheet = tallySheet;
        this.increments = increments;
    }

    public static RestTallyResponse of(RestTallySheet tallySheet, RestIncrements increments) {
        Preconditions.checkArgument(tallySheet != null, "tallySheet must not be null");
        Preconditions.checkArgument(increments != null, "increments must not be null");
        return new RestTallyResponse(tallySheet, increments);
    }

    public RestTallySheet getTallySheet() {
        return this.tallySheet;
    }

    public RestIncrements getIncrements() {
        return this.increments;
    }

}
