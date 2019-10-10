package de.skuzzle.tally;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}
