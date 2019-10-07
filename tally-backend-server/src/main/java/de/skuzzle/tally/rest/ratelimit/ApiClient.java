package de.skuzzle.tally.rest.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

public interface ApiClient {

    boolean exceedsLimitOf(RateLimiter rateLimiter);

}
