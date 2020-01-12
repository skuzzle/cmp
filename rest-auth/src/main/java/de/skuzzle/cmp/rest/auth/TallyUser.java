package de.skuzzle.cmp.rest.auth;

import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.google.common.base.Preconditions;

public interface TallyUser {

    public static TallyUser forTest(String id, boolean anonymous) {
        return new TallyUserImpl("test", id, anonymous);
    }

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
            return new TallyUserImpl("google", jwt.getClaimAsString("email"), false);
        } else if (authentication instanceof AnonymousAuthenticationToken) {
            return new TallyUserImpl("unknown", UUID.randomUUID().toString(), true);
        } else {
            return new TallyUserImpl("test", authentication.getName(), false);
        }
    }

    String getSource();

    String getId();

    boolean isAnonymous();

}