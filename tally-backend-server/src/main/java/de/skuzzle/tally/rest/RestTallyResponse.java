package de.skuzzle.tally.rest;

import com.google.common.base.Preconditions;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestIncrements increments;
    private final RestErrorMessage error;

    private RestTallyResponse(RestTallySheet tallySheet, RestIncrements increments, RestErrorMessage error) {
        this.tallySheet = tallySheet;
        this.increments = increments;
        this.error = error;
    }

    public static RestTallyResponse of(RestTallySheet tallySheet, RestIncrements increments) {
        Preconditions.checkArgument(tallySheet != null, "tallySheet must not be null");
        Preconditions.checkArgument(increments != null, "increments must not be null");
        return new RestTallyResponse(tallySheet, increments, null);
    }

    public static RestTallyResponse failure(String message, String origin) {
        return new RestTallyResponse(null, null, new RestErrorMessage(message, origin));
    }

    public RestTallySheet getTallySheet() {
        return this.tallySheet;
    }

    public RestIncrements getIncrements() {
        return this.increments;
    }

    public RestErrorMessage getError() {
        return this.error;
    }

}
