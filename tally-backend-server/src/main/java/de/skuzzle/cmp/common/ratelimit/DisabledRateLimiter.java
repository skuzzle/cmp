package de.skuzzle.cmp.common.ratelimit;

public class DisabledRateLimiter<T> implements ApiRateLimiter<T> {

    @Override
    public boolean exceedsLimit(T hint) {
        return false;
    }

    @Override
    public void blockIfRateLimitIsExceeded(T hint) {
        // do nothing
    }

}
