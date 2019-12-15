package de.skuzzle.cmp.ratelimit;

public interface ApiRateLimiter<T> {

    boolean exceedsLimit(T hint);

    void blockIfRateLimitIsExceeded(T hint);

}