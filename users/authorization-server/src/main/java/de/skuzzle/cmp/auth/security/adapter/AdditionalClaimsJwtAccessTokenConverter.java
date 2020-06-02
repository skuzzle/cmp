package de.skuzzle.cmp.auth.security.adapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class AdditionalClaimsJwtAccessTokenConverter extends JwtAccessTokenConverter {

    private final Collection<TokenAdapter> adapters;

    public AdditionalClaimsJwtAccessTokenConverter(Collection<TokenAdapter> adapters) {
        this.adapters = adapters;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken token = prepareToken(accessToken, authentication);
        final Object principal = authentication.getUserAuthentication().getPrincipal();

        final Map<String, Object> claims = selectAdapterFor(principal)
                .map(adapter -> adapter.getAdditionalClaims(principal))
                .orElseThrow(
                        () -> new IllegalStateException("Could not enhance token claims for principal: " + principal));
        token.getAdditionalInformation().putAll(claims);

        return super.enhance(token, authentication);
    }

    private Optional<TokenAdapter> selectAdapterFor(Object principal) {
        return adapters.stream()
                .filter(adapter -> adapter.getPrincipalType().isInstance(principal))
                .findFirst();
    }

    private DefaultOAuth2AccessToken prepareToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // some nasty copying because the original AdditionalInformation Map might be
        // unmodifiable
        final DefaultOAuth2AccessToken tokenCopy = defaultToken(accessToken);
        final Map<String, Object> copy = new HashMap<>(tokenCopy.getAdditionalInformation());
        tokenCopy.setAdditionalInformation(copy);
        return tokenCopy;
    }

    private DefaultOAuth2AccessToken defaultToken(OAuth2AccessToken token) {
        return token instanceof DefaultOAuth2AccessToken
                ? (DefaultOAuth2AccessToken) token
                : new DefaultOAuth2AccessToken(token);
    }

}