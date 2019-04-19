package de.skuzzle.tally.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

class ApiRateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(ApiRateLimiter.class);

    private final LoadingCache<String, RateLimiter> limiterCache;

    ApiRateLimiter(double rateLimit) {
        this.limiterCache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String clientIp) throws Exception {
                        logger.debug("Create new rate limiter for ip '{}'", clientIp);
                        return RateLimiter.create(rateLimit);
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
