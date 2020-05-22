package de.skuzzle.cmp.auth;

import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.google.common.base.Preconditions;

class AuthenticatedTallyUser implements TallyUser {

    private final OidcUser openIdUser;

    private AuthenticatedTallyUser(OidcUser openIdUser) {
        Preconditions.checkArgument(openIdUser != null, "openIdUser must not be null");
        this.openIdUser = openIdUser;
    }

    static TallyUser fromOpenIdUser(OidcUser openIdUser) {
        return new AuthenticatedTallyUser(openIdUser);
    }

    @Override
    public Optional<String> getAccessToken() {
        return Optional.of(openIdUser)
                .map(OidcUser::getIdToken)
                .map(OidcIdToken::getTokenValue)
                .map(Object::toString);
    }

    @Override
    public String getName() {
        return openIdUser.getFullName();
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
