package de.skuzzle.cmp.counter.rest;

class RestErrorMessage {
    private final String message;
    private final String origin;

    private RestErrorMessage(String message, String origin) {
        this.message = message;
        this.origin = origin;
    }
    
    public static RestErrorMessage of(String message, String origin) {
        return new RestErrorMessage(message, origin);
    }

    public String getMessage() {
        return message;
    }

    public String getOrigin() {
        return origin;
    }
}
