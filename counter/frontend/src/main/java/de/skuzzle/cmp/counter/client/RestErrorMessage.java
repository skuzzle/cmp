package de.skuzzle.cmp.counter.client;

public class RestErrorMessage {
    private final String message;
    private final String origin;
    private final String requestId;

    RestErrorMessage(String message, String origin, String requestId) {
        this.message = message;
        this.origin = origin;
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public String getOrigin() {
        return origin;
    }

    public String getRequestId() {
        return this.requestId;
    }
}
