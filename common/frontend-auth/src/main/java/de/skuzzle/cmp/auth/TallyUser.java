package de.skuzzle.cmp.auth;

import java.util.Optional;

public interface TallyUser {

    Optional<String> getAccessToken();

    String getName();

    boolean isLoggedIn();
}
