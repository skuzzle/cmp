package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "mongoUtcDateTimeProvider")
public class TallyServiceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TallyServiceConfiguration.class);

    private final ApiProperties apiProperties;

    public TallyServiceConfiguration(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public DateTimeProvider mongoUtcDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
    }

    @Bean
    public ApiRateLimiter rateLimiter() {
        logger.info("Configuring API rate limiter with a client dependent rate limit of {}", apiProperties.getRateLimit());
        return new ApiRateLimiter(apiProperties.getRateLimit());
    }
}
