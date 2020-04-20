package de.skuzzle.cmp.users.security;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.skuzzle.cmp.common.time.UTCDateTimeProvider;
import de.skuzzle.cmp.users.registration.RegisterUserService;
import de.skuzzle.cmp.users.registration.RegisteredUser;

class CmpUserDetailsService implements UserDetailsService {

    private final RegisterUserService registeredUserService;

    public CmpUserDetailsService(RegisterUserService registeredUserService) {
        this.registeredUserService = registeredUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final RegisteredUser user = registeredUserService.findUser(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        final LocalDateTime now = UTCDateTimeProvider.getInstance().getNowLocal();
        return User.builder()
                .accountLocked(user.block().isBlocked(now))
                .authorities(user.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()))
                .disabled(!user.registrationConfirmation().isConfirmed())
                .username(username)
                .password(user.password().hashedPassword())
                .build();
    }

}
