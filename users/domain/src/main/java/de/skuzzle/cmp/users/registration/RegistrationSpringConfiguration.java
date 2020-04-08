package de.skuzzle.cmp.users.registration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RegistrationSpringConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UTCDateTimeProvider dateTimeProvider() {
        return UTCDateTimeProvider.getInstance();
    }

    @Bean
    public RegisterUserService registerUserService(RegisteredUserRepository repository) {
        return new RegisterUserService(passwordEncoder(), dateTimeProvider(), repository);
    }

}
