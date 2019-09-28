package de.skuzzle.tally.rest.ratelimit;

import java.util.Objects;

import com.google.common.util.concurrent.RateLimiter;

public final class SimpleApiClient implements ApiClient {

    private final Object key;

    SimpleApiClient(Object key) {
        this.key = key;
    }

    @Override
    public boolean exceedsLimitOf(RateLimiter rateLimiter) {
        return !rateLimiter.tryAcquire(1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof SimpleApiClient
                && Objects.equals(key, ((SimpleApiClient) obj).key);
    }

    @Override
    public String toString() {
        return "ApiClient: " + key;
    }
}
