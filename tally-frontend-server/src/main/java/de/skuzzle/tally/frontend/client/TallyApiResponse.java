package de.skuzzle.tally.frontend.client;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public class TallyApiResponse {

    private final HttpStatus status;
    private final TallySheet tallySheet;
    private final ErrorResponse errorResponse;

    public TallyApiResponse(HttpStatus status, TallySheet tallySheet, ErrorResponse errorResponse) {
        this.status = status;
        this.tallySheet = tallySheet;
        this.errorResponse = errorResponse;
    }

    public static TallyApiResponse success(HttpStatus status, TallySheet tallySheet) {
        return new TallyApiResponse(status, tallySheet, null);
    }

    public static TallyApiResponse fail(HttpStatus status, ErrorResponse errorResponse) {
        return new TallyApiResponse(status, null, errorResponse);
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

    public Optional<TallySheet> tallySheet() {
        return Optional.ofNullable(tallySheet);
    }

    public Optional<ErrorResponse> error() {
        return Optional.ofNullable(errorResponse);
    }
}
