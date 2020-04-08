package de.skuzzle.cmp.rest.ratelimit;

class RestErrorMessage {
    private final String message;
    private final String origin;
    private final String requestId;

    private RestErrorMessage(String message, String origin, String requestId) {
        this.message = message;
        this.origin = origin;
        this.requestId = requestId;
    }

    public static RestErrorMessage of(String message, String origin, String requestId) {
        return new RestErrorMessage(message, origin, requestId);
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
