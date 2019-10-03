package de.skuzzle.tally.frontend.client;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestIncrements increments;
    private final RestErrorMessage error;

    private RestTallyResponse(RestTallySheet tallySheet, RestIncrements increments, RestErrorMessage error) {
        this.tallySheet = tallySheet;
        this.increments = increments;
        this.error = error;
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
