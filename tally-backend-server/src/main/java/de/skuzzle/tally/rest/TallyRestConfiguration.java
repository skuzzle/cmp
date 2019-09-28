package de.skuzzle.tally.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.skuzzle.tally.service.TallyServiceConfiguration;

@Configuration
public class TallyRestConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TallyServiceConfiguration.class);

    private final ApiProperties apiProperties;

    public TallyRestConfiguration(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public ApiRateLimiter rateLimiter() {
        logger.info("Configuring API rate limiter with a client dependent rate limit of {}",
                apiProperties.getRequestsPerMinute());
        return new ApiRateLimiter(apiProperties.getRequestsPerMinute());
    }
}
