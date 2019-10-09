package de.skuzzle.tally.auth;

import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.google.common.base.Preconditions;

public class TallyUser {

    private final String userId;

    private TallyUser(String userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        Preconditions.checkArgument(!userId.isEmpty(), "userId must not be empty");
        this.userId = userId;
    }

    public static TallyUser withId(String userId) {
        return new TallyUser(userId);
    }

    public static TallyUser fromCurrentRequestContext() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        final String userId = getIdFrom(authentication);
        return withId(userId);
    }

    private static String getIdFrom(Authentication authentication) {
        Preconditions.checkArgument(authentication != null,
                "no Authentication object available. Check your Spring-Security configuration");

        final Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            final Jwt jwt = (Jwt) principal;
            final String id = "google:" + jwt.getClaimAsString("email");
            return id;
        } else if (authentication instanceof AnonymousAuthenticationToken) {
            final String id = "unknown:" + UUID.randomUUID().toString();
            return id;
        } else {
            return authentication.getName();
        }
    }

    public String getUserId() {
        return userId;
    }
    
    public boolean isAnonymous() {
        return !userId.startsWith("google:");
    }
}
