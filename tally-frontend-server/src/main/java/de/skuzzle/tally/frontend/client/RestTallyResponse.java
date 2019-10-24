package de.skuzzle.tally.frontend.client;

public class RestTallyResponse {

    private final RestTallySheet tallySheet;
    private final RestIncrements increments;

    RestTallyResponse(RestTallySheet tallySheet, RestIncrements increments) {
        this.tallySheet = tallySheet;
        this.increments = increments;
    }

    public RestTallySheet getTallySheet() {
        return this.tallySheet;
    }

    public RestIncrements getIncrements() {
        return this.increments;
    }
}
