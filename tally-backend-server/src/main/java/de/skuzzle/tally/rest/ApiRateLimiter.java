package de.skuzzle.tally.rest;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;

class ApiRateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(ApiRateLimiter.class);

    private final LoadingCache<String, RateLimiter> limiterCache;

    ApiRateLimiter(double requestsPerMinute) {
        this.limiterCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String clientIp) throws Exception {
                        logger.debug("Create new rate limiter for ip '{}'", clientIp);
                        return RateLimiter.create(requestsPerMinute);
                    }
                });
    }

    public void throttle(HttpServletRequest request) {
        final String clientIP = getClientIP(request);
        final RateLimiter rateLimiter = limiterCache.getUnchecked(clientIP);
        if (!rateLimiter.tryAcquire(1)) {
            throw new RateLimitExceededException();
        }
    }

    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
