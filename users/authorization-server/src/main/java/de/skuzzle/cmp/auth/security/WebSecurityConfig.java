package de.skuzzle.cmp.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import de.skuzzle.cmp.auth.security.adapter.cmp.CmpUserDetailsService;
import de.skuzzle.cmp.users.registration.RegisterUserService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RegisterUserService registerUserService;

    public WebSecurityConfig(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                .oauth2Login()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    @Bean
    public UserDetailsService cmpUserDetailsService() {
        return new CmpUserDetailsService(registerUserService);
    }

}
