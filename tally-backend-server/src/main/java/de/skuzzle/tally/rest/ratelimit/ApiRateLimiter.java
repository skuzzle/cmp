package de.skuzzle.tally.rest.ratelimit;

public interface ApiRateLimiter<T> {

    boolean exceedsLimit(T hint);

    void blockIfRateLimitIsExceeded(T hint);

}