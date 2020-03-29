package de.skuzzle.cmp.common.ratelimit;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;

import io.micrometer.core.instrument.Metrics;

public class CachedRateLimiter<T> implements ApiRateLimiter<T> {

    private static final Logger logger = LoggerFactory.getLogger(CachedRateLimiter.class);

    private final RateLimiterCache rateLimiterCache;
    private final ClientIdentificator<T> clientIdentificator;

    public CachedRateLimiter(ClientIdentificator<T> clientIdentificator, RateLimiterCache rateLimiterCache) {
        Preconditions.checkArgument(clientIdentificator != null, "clientIdentificator must not be null");

        this.clientIdentificator = clientIdentificator;
        this.rateLimiterCache = rateLimiterCache;
    }

    @Override
    public boolean exceedsLimit(RateLimitedOperation operation, T hint) {
        Preconditions.checkArgument(operation != null, "operation must not be null");
        Preconditions.checkArgument(hint != null, "rate limiter client hint must not be null");

        final Optional<ApiClient> identifiedClient = clientIdentificator.tryIdentifyClientFrom(hint);
        if (identifiedClient.isPresent()) {
            final ApiClient client = identifiedClient.orElseThrow();
            final RateLimiter rateLimiter = rateLimiterCache.getRateLimiterForClient(client);
            return client.exceedsLimitOf(rateLimiter, operation);
        }
        // can not exceed the limit if client has not been identified
        logger.debug("No client could be identified from '{}'. This request was not rate limited!", hint);
        return false;
    }

    @Override
    public void blockIfRateLimitIsExceeded(RateLimitedOperation operation, T hint) {
        Preconditions.checkArgument(operation != null, "operation must not be null");
        Preconditions.checkArgument(hint != null, "rate limiter client hint must not be null");

        clientIdentificator.tryIdentifyClientFrom(hint).ifPresent(client -> {
            final RateLimiter rateLimiter = rateLimiterCache.getRateLimiterForClient(client);
            if (client.exceedsLimitOf(rateLimiter, operation)) {
                logger.warn("Request from {} has been blocked after exceeding rate limit", client);
                Metrics.counter("rate_limit_exceeded", "user_id", client.toString()).increment();
                throw new RateLimitExceededException(client);
            }
        });
    }

}
