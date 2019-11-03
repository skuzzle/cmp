package de.skuzzle.cmp.frontend.client;

public class RestErrorMessage {
    private final String message;
    private final String origin;

    RestErrorMessage(String message, String origin) {
        this.message = message;
        this.origin = origin;
    }

    public String getMessage() {
        return message;
    }

    public String getOrigin() {
        return origin;
    }
}
