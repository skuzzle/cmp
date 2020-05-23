package de.skuzzle.cmp.auth.security;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public abstract class AdditionalClaimsJwtAccessTokenConverter<T extends UserDetails> extends JwtAccessTokenConverter {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalClaimsJwtAccessTokenConverter.class);

    protected abstract Class<T> getUserDetailsType();

    protected abstract Map<String, Object> getAdditionalClaims(T userDetails);

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken token = prepareToken(accessToken, authentication);
        final Object principal = authentication.getUserAuthentication().getPrincipal();

        if (getUserDetailsType().isInstance(principal)) {
            final Map<String, Object> additionalClaims = getAdditionalClaims(getUserDetailsType().cast(principal));
            token.getAdditionalInformation().putAll(additionalClaims);
        } else {
            logger.debug(
                    "Principal '{}' wasn't elligible for being enhanced with additional claims because it is not of type {}",
                    principal, getUserDetailsType());
        }

        return super.enhance(token, authentication);
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