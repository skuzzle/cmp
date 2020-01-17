package de.skuzzle.cmp.auth;

import java.util.Optional;

public interface TallyUser {

    Optional<String> getOidToken();

    String getName();

    boolean isLoggedIn();
}
