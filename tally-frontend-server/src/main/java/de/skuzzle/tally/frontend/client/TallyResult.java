package de.skuzzle.tally.frontend.client;

import java.util.Optional;

import org.springframework.http.HttpStatus;

public class TallyResult<T> {

    private final HttpStatus status;
    private final T payload;
    private final RestErrorMessage errorResponse;

    private TallyResult(HttpStatus status, T payload, RestErrorMessage errorResponse) {
        this.status = status;
        this.payload = payload;
        this.errorResponse = errorResponse;
    }

    public static <T> TallyResult<T> success(HttpStatus status, T payload) {
        return new TallyResult<T>(status, payload, null);
    }

    public static <T> TallyResult<T> fail(HttpStatus status, RestErrorMessage errorResponse) {
        return new TallyResult<>(status, null, errorResponse);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return payload != null;
    }

    public boolean isError() {
        return errorResponse != null;
    }

    public Optional<T> payload() {
        return Optional.ofNullable(payload);
    }

    public Optional<RestErrorMessage> error() {
        return Optional.ofNullable(errorResponse);
    }
}
