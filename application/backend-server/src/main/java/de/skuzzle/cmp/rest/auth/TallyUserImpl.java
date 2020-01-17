package de.skuzzle.cmp.rest.auth;

import java.util.Objects;
import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.google.common.base.Preconditions;

final class TallyUserImpl implements TallyUser {

    private final String source;
    private final String id;
    private final boolean anonymous;

    TallyUserImpl(String source, String id, boolean anonymous) {
        Preconditions.checkArgument(source != null, "source must not be null");
        Preconditions.checkArgument(id != null, "id must not be null");
        this.source = source;
        this.id = id;
        this.anonymous = anonymous;
    }

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

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isAnonymous() {
        return anonymous;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TallyUser
                && Objects.equals(getSource(), ((TallyUser) obj).getSource())
                && Objects.equals(getId(), ((TallyUserImpl) obj).getId());
    }

    @Override
    public String toString() {
        return String.format("%s:%s", source, id);
    }
}
