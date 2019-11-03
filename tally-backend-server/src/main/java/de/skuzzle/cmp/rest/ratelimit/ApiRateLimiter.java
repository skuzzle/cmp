package de.skuzzle.cmp.rest.ratelimit;

public interface ApiRateLimiter<T> {

    boolean exceedsLimit(T hint);

    void blockIfRateLimitIsExceeded(T hint);

}