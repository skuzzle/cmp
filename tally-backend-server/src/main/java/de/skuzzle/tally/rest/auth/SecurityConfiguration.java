package de.skuzzle.tally.rest.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll().and()
                .cors().disable()
                .csrf().disable()
                .oauth2ResourceServer().jwt().jwkSetUri("https://www.googleapis.com/oauth2/v3/certs").and()
                .and()
                .anonymous();
    }
}
