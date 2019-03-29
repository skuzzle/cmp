package de.skuzzle.tally.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "xxx")
public class TallyServiceConfiguration {

    @Bean
    public DateTimeProvider xxx() {
        return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
