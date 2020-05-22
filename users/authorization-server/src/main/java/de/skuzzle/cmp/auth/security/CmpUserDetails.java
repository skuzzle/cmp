package de.skuzzle.cmp.auth.security;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.common.time.ApplicationClock;
import de.skuzzle.cmp.users.registration.RegisteredUser;

public class CmpUserDetails extends User {

    private static final long serialVersionUID = 4808997951511566854L;

    private final String fullName;

    private CmpUserDetails(String username,
            String password,
            String fullName,
            boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.fullName = fullName;
    }

    public static CmpUserDetails fromRegisteredUser(RegisteredUser user) {
        Preconditions.checkArgument(user != null, "user must not be null");
        final LocalDateTime now = ApplicationClock.now();
        final boolean enabled = user.registrationConfirmation().isConfirmed();
        final boolean accountExpired = false;
        final boolean credentialsExpired = false;
        final boolean accountLocked = user.block().isBlocked(now);

        final Set<SimpleGrantedAuthority> authorities = user.authorities().stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new CmpUserDetails(
                user.email(),
                user.password().hashedPassword(),
                user.name(),
                enabled,
                !accountExpired,
                !credentialsExpired,
                !accountLocked,
                authorities);
    }

    public String getFullName() {
        return this.fullName;
    }
}
