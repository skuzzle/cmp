package de.skuzzle.cmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.mobile.device.annotation.DeviceResolverConfiguration;

import io.micrometer.core.instrument.Metrics;

@SpringBootApplication
@EnableConfigurationProperties(Version.class)
@Import(DeviceResolverConfiguration.class)
public class TallyFrontendApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TallyFrontendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TallyFrontendApplication.class, args);
    }

    @Autowired(required = false)
    private void reportVersionNumber(Version version) {
        LOGGER.info("Running version '{}'", version.getVersion());
        Metrics.counter("version_name", "version", version.getVersion()).increment();
    }

}
