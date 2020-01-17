package de.skuzzle.cmp.counter.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Metrics;

@Configuration
@EnableConfigurationProperties(Version.class)
public class VersionSpringConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionSpringConfiguration.class);

    @Autowired(required = false)
    private void reportVersionNumber(Version version) {
        LOGGER.info("Running version '{}'", version.getVersion());
        Metrics.counter("version_name", "version", version.getVersion()).increment();
    }
}
