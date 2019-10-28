package de.skuzzle.tally.rest.ratelimit;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;

import io.micrometer.core.instrument.Metrics;

public class MemoryCacheRateLimiter<T> implements ApiRateLimiter<T> {

    private static final Logger logger = LoggerFactory.getLogger(MemoryCacheRateLimiter.class);

    private static final long REMEMBER_CLIENTS_FOR = 5; // minutes

    private final LoadingCache<ApiClient, RateLimiter> limiterCache;
    private final ClientIdentificator<T> clientIdentificator;

    public MemoryCacheRateLimiter(ClientIdentificator<T> clientIdentificator, double requestsPerSecond) {
        this.clientIdentificator = clientIdentificator;
        this.limiterCache = CacheBuilder.newBuilder()
                .expireAfterAccess(REMEMBER_CLIENTS_FOR, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<ApiClient, RateLimiter>() {
                    @Override
                    public RateLimiter load(ApiClient clientIp) throws Exception {
                        logger.debug("Create new rate limiter for client '{}' with {} QPS", clientIp,
                                requestsPerSecond);
                        return RateLimiter.create(requestsPerSecond);
                    }
                });
    }

    @Override
    public boolean exceedsLimit(T hint) {
        Preconditions.checkArgument(hint != null, "rate limiter client hint must not be null");
        final Optional<ApiClient> identifiedClient = clientIdentificator.tryIdentifyClientFrom(hint);
        if (identifiedClient.isPresent()) {
            final ApiClient client = identifiedClient.orElseThrow();
            final RateLimiter rateLimiter = limiterCache.getUnchecked(client);
            return client.exceedsLimitOf(rateLimiter);
        }
        // can not exceed the limit if client has not been identified
        logger.debug("No client could be identified from '{}'. This request was not rate limited!", hint);
        return false;
    }

    @Override
    public void blockIfRateLimitIsExceeded(T hint) {
        Preconditions.checkArgument(hint != null, "rate limiter client hint must not be null");
        clientIdentificator.tryIdentifyClientFrom(hint).ifPresent(client -> {
            final RateLimiter rateLimiter = limiterCache.getUnchecked(client);
            if (client.exceedsLimitOf(rateLimiter)) {
                logger.warn("Request from {} has been blocked after exceeding rate limit", client);
                Metrics.counter("rate_limit_exceeded", "user_id", client.toString()).increment();
                throw new RateLimitExceededException(client);
            }
        });
    }

}
