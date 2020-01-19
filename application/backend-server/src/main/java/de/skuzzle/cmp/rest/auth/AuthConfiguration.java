package de.skuzzle.cmp.rest.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
class AuthConfiguration {

    @Bean
    @RequestScope
    public TallyUser tallyUser() {
        return TallyUserFactory.fromCurrentAuthentication();
    }
}
