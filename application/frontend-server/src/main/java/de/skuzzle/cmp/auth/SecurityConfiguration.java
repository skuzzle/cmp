package de.skuzzle.cmp.auth;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setUseReferer(true);

        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .oauth2Login().successHandler(successHandler)
                .and()
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    @RequestScope
    public TallyUser currentUser() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast)
                .map(AuthenticatedTallyUser::fromOpenIdUser)
                .orElseGet(AnonymousTallyUser::getInstance);
    }
}
