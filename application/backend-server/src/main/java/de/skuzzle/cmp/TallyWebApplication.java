package de.skuzzle.cmp;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.skuzzle.cmp.common.http.RequestIdFilter;
import de.skuzzle.cmp.common.http.RequestLoggingFilter;
import de.skuzzle.cmp.common.http.ResponseSizeTrackingFilter;
import io.micrometer.core.instrument.Metrics;

@SpringBootApplication
public class TallyWebApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TallyWebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TallyWebApplication.class, args);
    }

    @Autowired
    private void reportVersionNumber(@Value("${version.number}") String versionNumber) {
        LOGGER.info("Running version '{}'", versionNumber);
        Metrics.counter("version_name", "version", versionNumber).increment();
    }

    @Bean
    public Filter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    public Filter trackResponseSizes() {
        return new ResponseSizeTrackingFilter();
    }

    @Bean
    public Filter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }
}
