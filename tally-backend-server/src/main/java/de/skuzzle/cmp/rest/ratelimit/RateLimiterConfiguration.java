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
import de.skuzzle.cmp.counter.domain.TallyServiceConfiguration;
import de.skuzzle.cmp.rest.auth.TallyUser;
import de.skuzzle.cmp.rest.ratelimit.RateLimitApiProperties.Ratelimit;

@Configuration
public class RateLimiterConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TallyServiceConfiguration.class);

    private final RateLimitApiProperties apiProperties;

    public RateLimiterConfiguration(RateLimitApiProperties apiProperties) {
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

    private static class AuthenticationClientIdentificator implements ClientIdentificator<HttpServletRequest> {

        private final ClientIdentificator<HttpServletRequest> identifyByIp = new RemoteIpClientIdentificator();

        @Override
        public Optional<ApiClient> tryIdentifyClientFrom(HttpServletRequest hint) {
            final TallyUser user = TallyUser.fromCurrentAuthentication();
            if (user.isAnonymous()) {
                return identifyByIp.tryIdentifyClientFrom(hint);
            }
            return Optional.of(ApiClient.identifiedBy(user));
        }

    }
}
