package de.skuzzle.tally.rest;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.skuzzle.tally.rest.ApiProperties.Ratelimit;
import de.skuzzle.tally.rest.ratelimit.ApiRateLimiter;
import de.skuzzle.tally.rest.ratelimit.DisabledRateLimiter;
import de.skuzzle.tally.rest.ratelimit.MemoryCacheRateLimiter;
import de.skuzzle.tally.service.TallyServiceConfiguration;

@Configuration
public class TallyRestConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TallyServiceConfiguration.class);

    private final ApiProperties apiProperties;

    public TallyRestConfiguration(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public ApiRateLimiter<HttpServletRequest> rateLimiter() {
        final Ratelimit rateLimit = apiProperties.getRatelimit();
        if (rateLimit.isEnabled()) {
            logger.info("Configuring API rate limiter with a client dependent rate limit of {}", rateLimit.getRps());
            return new MemoryCacheRateLimiter<>(
                    new AuthenticationClientIdentificator(),
                    rateLimit.getRps());
        } else {
            logger.warn("API rate limit is disabled");
            return new DisabledRateLimiter<>();
        }

    }
}
