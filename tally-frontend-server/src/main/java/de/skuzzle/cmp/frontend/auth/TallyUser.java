package de.skuzzle.cmp.frontend.auth;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface TallyUser {

    static TallyUser fromCurrentRequestContext() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast)
                .map(AuthenticatedTallyUser::fromOpenIdUser)
                .orElseGet(AnonymousTallyUser::getInstance);
    }

    String getName();

    boolean isLoggedIn();
}
