package de.skuzzle.cmp.rest.auth;

import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.google.common.base.Preconditions;

final class TallyUserFactory {

    public static TallyUser fromCurrentAuthentication() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        return getIdFrom(authentication);
    }

    private static TallyUser getIdFrom(Authentication authentication) {
        Preconditions.checkArgument(authentication != null,
                "no Authentication object available. Check your Spring-Security configuration");

        final Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            final Jwt jwt = (Jwt) principal;

            if ("cmp".equals(jwt.getClaimAsString("client_id"))) {
                return TallyUser.create("cmp", jwt.getSubject(), false);
            }

            return TallyUser.create("google", jwt.getClaimAsString("email"), false);
        } else if (authentication instanceof AnonymousAuthenticationToken) {
            return TallyUser.unknownWithId(UUID.randomUUID().toString());
        } else {
            return TallyUser.forTestWithId(authentication.getName());
        }
    }

}
