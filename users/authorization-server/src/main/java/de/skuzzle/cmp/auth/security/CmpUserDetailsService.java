package de.skuzzle.cmp.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.skuzzle.cmp.users.registration.RegisterUserService;
import de.skuzzle.cmp.users.registration.RegisteredUser;

class CmpUserDetailsService implements UserDetailsService {

    private final RegisterUserService registeredUserService;

    public CmpUserDetailsService(RegisterUserService registeredUserService) {
        this.registeredUserService = registeredUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final RegisteredUser user = registeredUserService.findUser(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return CmpUserDetails.fromRegisteredUser(user);
    }

}
