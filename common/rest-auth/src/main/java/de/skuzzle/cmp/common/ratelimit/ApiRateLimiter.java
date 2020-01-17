package de.skuzzle.cmp.common.ratelimit;

public interface ApiRateLimiter<T> {

    boolean exceedsLimit(T hint);

    void blockIfRateLimitIsExceeded(T hint);

}