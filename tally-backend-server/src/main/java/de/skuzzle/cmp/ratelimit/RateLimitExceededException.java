package de.skuzzle.cmp.ratelimit;

public class RateLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 6658671292532942336L;

    private final ApiClient client;

    public RateLimitExceededException(ApiClient client) {
        this.client = client;
    }

    public ApiClient getClient() {
        return this.client;
    }
}
