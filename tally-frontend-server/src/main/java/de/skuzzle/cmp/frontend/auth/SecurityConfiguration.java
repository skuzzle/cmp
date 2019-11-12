package de.skuzzle.cmp.frontend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .and()
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    @RequestScope
    public TallyUser currentUser() {
        return TallyUser.fromCurrentRequestContext();
    }
}
