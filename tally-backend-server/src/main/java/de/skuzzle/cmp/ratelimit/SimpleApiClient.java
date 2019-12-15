package de.skuzzle.cmp.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

public final class SimpleApiClient extends BaseApiClient implements ApiClient {

    SimpleApiClient(Object key) {
        super(key);
    }

    @Override
    public boolean exceedsLimitOf(RateLimiter rateLimiter) {
        return !rateLimiter.tryAcquire(1);
    }
}
