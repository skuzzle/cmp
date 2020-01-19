package de.skuzzle.cmp.rest.ratelimit;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.skuzzle.cmp.common.ratelimit.ApiClient;
import de.skuzzle.cmp.common.ratelimit.ApiRateLimiter;
import de.skuzzle.cmp.common.ratelimit.ClientIdentificator;
import de.skuzzle.cmp.common.ratelimit.DisabledRateLimiter;
import de.skuzzle.cmp.common.ratelimit.MemoryCacheRateLimiter;
import de.skuzzle.cmp.common.ratelimit.RemoteIpClientIdentificator;
import de.skuzzle.cmp.rest.auth.TallyUser;
import de.skuzzle.cmp.rest.ratelimit.RateLimitApiProperties.Ratelimit;

@Configuration
public class RateLimiterConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterConfiguration.class);

    private final RateLimitApiProperties apiProperties;

    public RateLimiterConfiguration(RateLimitApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public ApiRateLimiter<HttpServletRequest> rateLimiter(TallyUser tallyUser) {
        final Ratelimit rateLimit = apiProperties.getRatelimit();
        if (rateLimit.isEnabled()) {
            logger.info("Configuring API rate limiter with a client dependent rate limit of {}", rateLimit.getRps());
            return new MemoryCacheRateLimiter<>(
                    new AuthenticationClientIdentificator(tallyUser),
                    rateLimit.getRps());
        } else {
            logger.warn("API rate limit is disabled");
            return new DisabledRateLimiter<>();
        }
    }

    private static class AuthenticationClientIdentificator implements ClientIdentificator<HttpServletRequest> {

        private final ClientIdentificator<HttpServletRequest> identifyByIp = new RemoteIpClientIdentificator();

        private final TallyUser tallyUser;

        AuthenticationClientIdentificator(TallyUser tallyUser) {
            this.tallyUser = tallyUser;
        }

        @Override
        public Optional<ApiClient> tryIdentifyClientFrom(HttpServletRequest hint) {
            if (tallyUser.isAnonymous()) {
                return identifyByIp.tryIdentifyClientFrom(hint);
            }
            return Optional.of(ApiClient.identifiedBy(tallyUser));
        }

    }
}
