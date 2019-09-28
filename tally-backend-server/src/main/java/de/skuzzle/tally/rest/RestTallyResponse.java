package de.skuzzle.tally.rest;

import com.google.common.base.Preconditions;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestErrorMessage error;

    private RestTallyResponse(RestTallySheet tallySheet, RestErrorMessage error) {
        this.tallySheet = tallySheet;
        this.error = error;
    }

    public static RestTallyResponse of(RestTallySheet tallySheet) {
        Preconditions.checkArgument(tallySheet != null, "tallySheet must not be null");
        return new RestTallyResponse(tallySheet, null);
    }

    public static RestTallyResponse failure(String message, String origin) {
        return new RestTallyResponse(null, new RestErrorMessage(message, origin));
    }

    public RestTallySheet getTallySheet() {
        return this.tallySheet;
    }

    public RestErrorMessage getError() {
        return this.error;
    }

}
