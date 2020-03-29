package de.skuzzle.cmp.common.ratelimit;

public interface ApiRateLimiter<T> {

    boolean exceedsLimit(RateLimitedOperation operation, T hint);

    void blockIfRateLimitIsExceeded(RateLimitedOperation operation, T hint);

}