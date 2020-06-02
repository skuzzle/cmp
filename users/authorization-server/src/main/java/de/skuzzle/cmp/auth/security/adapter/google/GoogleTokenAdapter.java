package de.skuzzle.cmp.auth.security.adapter.google;

import java.util.Map;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import de.skuzzle.cmp.auth.security.adapter.TokenAdapter;

@Component
public class GoogleTokenAdapter implements TokenAdapter {

    @Override
    public Class<OidcUser> getPrincipalType() {
        return OidcUser.class;
    }

    @Override
    public Map<String, Object> getAdditionalClaims(Object principal) {
        final OidcUser googleUserDetails = (OidcUser) principal;
        return Map.of(
                "sub", googleUserDetails.getEmail(),
                "full_name", googleUserDetails.getFullName(),
                "account_type", "google");
    }

}
