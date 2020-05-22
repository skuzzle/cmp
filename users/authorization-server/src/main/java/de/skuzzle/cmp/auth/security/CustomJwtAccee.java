package de.skuzzle.cmp.auth.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccee extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) super.enhance(accessToken, authentication);

        // final CmpUserDetails user = (CmpUserDetails)
        // authentication.getUserAuthentication().getPrincipal();
        // token.getAdditionalInformation().put("sub", user.getFullName());
        return token;
    }
}
