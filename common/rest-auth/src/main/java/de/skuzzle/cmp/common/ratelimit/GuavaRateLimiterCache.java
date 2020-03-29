package de.skuzzle.cmp.common.ratelimit;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;

public class GuavaRateLimiterCache implements RateLimiterCache {

    private static final Logger logger = LoggerFactory.getLogger(GuavaRateLimiterCache.class);

    /*
     * If a recognized client does not send another request within this time its rate
     * limiter will be reset to zero
     */
    private static final long REMEMBER_CLIENTS_FOR = 5; // minutes
    private final LoadingCache<ApiClient, RateLimiter> limiterCache;

    public GuavaRateLimiterCache(double requestsPerSecond) {
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
    public RateLimiter getRateLimiterForClient(ApiClient client) {
        return limiterCache.getUnchecked(client);
    }
}
