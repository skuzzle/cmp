package de.skuzzle.tally.frontend.client;

import java.util.List;

import org.springframework.http.HttpStatus;

public class TallyListResult {
    private final HttpStatus status;
    private final List<RestTallySheet> tallySheets;
    private final RestErrorMessage errorResponse;

    private TallyListResult(HttpStatus status, List<RestTallySheet> tallySheets, RestErrorMessage errorResponse) {
        this.status = status;
        this.tallySheets = tallySheets;
        this.errorResponse = errorResponse;
    }

    public static TallyListResult success(HttpStatus status, RestTallySheetsReponse response) {
        return new TallyListResult(status, response.getTallySheets(), null);
    }

    public static TallyListResult fail(HttpStatus status, RestErrorMessage errorResponse) {
        return new TallyListResult(status, null, errorResponse);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return tallySheets != null;
    }

    public boolean isError() {
        return errorResponse != null;
    }

    public List<RestTallySheet> getTallySheets() {
        return tallySheets;
    }

}
