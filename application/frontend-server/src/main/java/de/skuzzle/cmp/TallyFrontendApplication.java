package de.skuzzle.cmp;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mobile.device.annotation.DeviceResolverConfiguration;

import de.skuzzle.cmp.common.http.ResponseSizeTrackingFilter;

@SpringBootApplication
@Import(DeviceResolverConfiguration.class)
public class TallyFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TallyFrontendApplication.class, args);
    }

    @Bean
    public Filter trackResponseSizes() {
        return new ResponseSizeTrackingFilter();
    }

}
