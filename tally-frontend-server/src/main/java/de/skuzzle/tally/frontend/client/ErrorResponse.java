package de.skuzzle.tally.frontend.client;

public class ErrorResponse {

    private String message;
    private String origin;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, String origin) {
        this.message = message;
        this.origin = origin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}

