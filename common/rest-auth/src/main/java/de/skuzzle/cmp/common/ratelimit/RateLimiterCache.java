package de.skuzzle.cmp.common.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

public interface RateLimiterCache {
    RateLimiter getRateLimiterForClient(ApiClient client);
}
