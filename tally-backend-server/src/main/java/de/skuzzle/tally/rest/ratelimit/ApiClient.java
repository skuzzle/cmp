package de.skuzzle.tally.rest.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

public interface ApiClient {
    
    public static ApiClient identifiedBy(Object key) {
        return new SimpleApiClient(key);
    }
    
    boolean exceedsLimitOf(RateLimiter rateLimiter);

}
