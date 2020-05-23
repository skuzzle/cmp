package de.skuzzle.cmp.auth.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@Import({ AuthorizationServerEndpointsConfiguration.class })
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthServerConfig(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) throws Exception {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .userDetailsService(userDetailsService)
                .accessTokenConverter(accessTokenConverter())
                .userApprovalHandler(userApprovalHandler())
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("cmp")
                // TODO: check what this flag actually does and whether it could replace
                // the customized userApprovalHandler. A first test revealed that
                // autoApprove=true doesn't seem to prevent the Approval page to show up
                // .autoApprove(true)
                .secret(passwordEncoder.encode("abc"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("read:user")
                .redirectUris(
                        "http://127.0.0.1:8081/login/oauth2/code/cmp",
                        "http://localhost:8081/login/oauth2/code/cmp",
                        "https://countmy.pizza/login/oauth2/code/cmp");
    }

    @Bean
    public UserApprovalHandler userApprovalHandler() {
        return new DefaultUserApprovalHandler() {
            @Override
            public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
                    Authentication userAuthentication) {
                // setting authorizationRequest.setApproved(true); will effectively skip
                // the
                // approval page after the successful login
                authorizationRequest.setApproved(true);
                return super.checkForPreApproval(authorizationRequest, userAuthentication);
            }
        };
    }

    // TODO: read from keystore file
    @Bean
    public KeyPair keypair() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            return keyPairGenerator.generateKeyPair();
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new CmpJwtAccessTokenEnhancer();
        converter.setKeyPair(keypair());
        return converter;
    }

}
