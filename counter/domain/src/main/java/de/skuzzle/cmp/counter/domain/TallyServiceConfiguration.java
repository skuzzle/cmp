package de.skuzzle.cmp.counter.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = TallyServiceConfiguration.TIME_PROVIDER_BEAN_NAME)
public class TallyServiceConfiguration {

    static final String TIME_PROVIDER_BEAN_NAME = "mongoUtcDateTimeProvider";

    @Bean(TIME_PROVIDER_BEAN_NAME)
    public DateTimeProvider mongoUtcDateTimeProvider() {
        return new MongoDateTimeProvider();
    }
}
