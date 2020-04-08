package de.skuzzle.cmp.common.ratelimit;

public class DisabledRateLimiter<T> implements ApiRateLimiter<T> {

    @Override
    public boolean exceedsLimit(RateLimitedOperation operation, T hint) {
        return false;
    }

    @Override
    public void blockIfRateLimitIsExceeded(RateLimitedOperation operation, T hint) {
        // do nothing
    }

}
