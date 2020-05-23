package de.skuzzle.cmp.auth;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        // https://github.com/skuzzle/cmp/issues/40
        // If we setUseReferer(true) the user is again redirected to the Oauth provider's
        // page because that is the referer of the last request that we received before
        // authentication completes
        // Need to find another way of redirecting the user to the page from where he came

        // successHandler.setUseReferer(true);

        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .oauth2Login(oauth2Login -> oauth2Login
                        // TODO: the referer stuff is problematic with the oauth flow
                        // .successHandler(successHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2UserService())))
                .oauth2Client(oauth2Client -> oauth2Client.authorizationCodeGrant())
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        final DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build());
        return authorizedClientManager;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        final DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
        userService.setRequestEntityConverter(new CustomParameterOAuth2UserRequestEntityConverter("token"));
        return userService;
    }

    @Bean
    @RequestScope
    public TallyUser currentUser(OAuth2AuthorizedClientService authorizedClientService,
            OAuth2AuthorizedClientManager clientManager) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            final OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
            final OAuth2User principal = oauth.getPrincipal();
            final OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    oauth.getAuthorizedClientRegistrationId(),
                    oauth.getName());

            if (authorizedClient == null) {
                return AnonymousTallyUser.getInstance();
            }

            final String token = authorizedClient.getAccessToken().getTokenValue();
            final String userName = principal.getAttribute("sub");
            return new SimpleAuthenticatedTallyUser(userName, token);
        }

        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast)
                .map(AuthenticatedTallyUser::fromOpenIdUser)
                .orElseGet(AnonymousTallyUser::getInstance);
    }
}
