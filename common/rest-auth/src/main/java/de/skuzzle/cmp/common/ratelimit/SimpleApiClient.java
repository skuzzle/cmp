package de.skuzzle.cmp.common.ratelimit;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;

final class SimpleApiClient implements ApiClient {

    protected final Object key;

    SimpleApiClient(Object key) {
        Preconditions.checkArgument(key != null, "key for identifying a client must not be null");
        this.key = key;
    }

    @Override
    public boolean exceedsLimitOf(RateLimiter rateLimiter, RateLimitedOperation operation) {
        final int costs = operation.costs();
        return costs > 0 && !rateLimiter.tryAcquire(costs);
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
