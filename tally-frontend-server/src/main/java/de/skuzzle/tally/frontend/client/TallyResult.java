package de.skuzzle.tally.frontend.client;

import java.util.Optional;

import org.springframework.http.HttpStatus;

public class TallyResult {

    private final HttpStatus status;
    private final RestTallySheet tallySheet;
    private final RestIncrements increments;
    private final RestErrorMessage errorResponse;

    TallyResult(HttpStatus status, RestTallySheet tallySheet, RestIncrements increments,
            RestErrorMessage errorResponse) {
        this.status = status;
        this.tallySheet = tallySheet;
        this.increments = increments;
        this.errorResponse = errorResponse;
    }

    public static TallyResult success(HttpStatus status, RestTallyResponse response) {
        return new TallyResult(status, response.getTallySheet(), response.getIncrements(), null);
    }

    public static TallyResult fail(HttpStatus status, RestErrorMessage errorResponse) {
        return new TallyResult(status, null, null, errorResponse);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return tallySheet != null;
    }

    public boolean isError() {
        return errorResponse != null;
    }

    public Optional<RestTallySheet> tallySheet() {
        return Optional.ofNullable(tallySheet);
    }

    public Optional<RestIncrements> increments() {
        return Optional.ofNullable(increments);
    }

    public Optional<RestErrorMessage> error() {
        return Optional.ofNullable(errorResponse);
    }
}
