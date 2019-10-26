package de.skuzzle.tally.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.micrometer.core.instrument.Metrics;

@SpringBootApplication
public class TallyFrontendApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TallyFrontendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TallyFrontendApplication.class, args);
    }

    @Autowired
    private void reportVersionNumber(Version version) {
        LOGGER.info("Running version '{}'", version.getVersion());
        Metrics.counter("version_name", "version", version.getVersion()).increment();
    }

}
