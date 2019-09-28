package de.skuzzle.tally.frontend.client;

class TallyApiResponse {

    private final TallySheet tallySheet;
    private final ErrorResponse error;

    public TallyApiResponse(TallySheet tallySheet, ErrorResponse error) {
        this.tallySheet = tallySheet;
        this.error = error;
    }

    public TallySheet getTallySheet() {
        return this.tallySheet;
    }

    public ErrorResponse getError() {
        return this.error;
    }

}
