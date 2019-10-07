package de.skuzzle.tally.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class TallyFrontendApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TallyFrontendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TallyFrontendApplication.class, args);
    }

    @Autowired
    private void reportVersionNumber(MeterRegistry meterRegistry, @Value("${version.number}") String versionNumber) {
        LOGGER.info("Running version '{}'", versionNumber);
        Counter.builder("version_name")
                .description("Deployed version")
                .tag("version", versionNumber)
                .register(meterRegistry)
                .increment();
    }

}
