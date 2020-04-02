package de.skuzzle.cmp.users.registration;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.skuzzle.cmp.common.time.UTCDateTimeProvider;

@Configuration
public class RegistrationSpringConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UTCDateTimeProvider dateTimeProvider() {
        return UTCDateTimeProvider.getInstance();
    }

    @Bean
    public RegisterUserService registerUserService(RegisteredUserRepository repository,
            ApplicationEventPublisher eventPublisher) {
        return new RegisterUserService(passwordEncoder(), dateTimeProvider(), repository, eventPublisher);
    }

}
