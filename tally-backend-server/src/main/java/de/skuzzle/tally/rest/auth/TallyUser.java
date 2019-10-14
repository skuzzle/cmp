package de.skuzzle.tally.rest.auth;

import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.google.common.base.Preconditions;

public class TallyUser {

    private final String source;
    private final String id;
    private final boolean anonymous;

    private TallyUser(String source, String id, boolean anonymous) {
        Preconditions.checkArgument(source != null, "source must not be null");
        Preconditions.checkArgument(id != null, "id must not be null");
        this.source = source;
        this.id = id;
        this.anonymous = anonymous;
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
            return new TallyUser("google", jwt.getClaimAsString("email"), false);
        } else if (authentication instanceof AnonymousAuthenticationToken) {
            return new TallyUser("unknown", UUID.randomUUID().toString(), true);
        } else {
            return new TallyUser("test", authentication.getName(), false);
        }
    }

    public String getSource() {
        return this.source;
    }

    public String getId() {
        return this.id;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", source, id);
    }
}
