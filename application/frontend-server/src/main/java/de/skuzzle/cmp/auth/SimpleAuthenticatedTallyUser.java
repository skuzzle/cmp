package de.skuzzle.cmp.auth;

import java.util.Optional;

import com.google.common.base.Preconditions;

class SimpleAuthenticatedTallyUser implements TallyUser {

    private final String name;
    private final String accessToken;

    SimpleAuthenticatedTallyUser(String name, String accessToken) {
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(accessToken != null, "accessToken must not be null");
        this.name = name;
        this.accessToken = accessToken;
    }

    @Override
    public Optional<String> getAccessToken() {
        return Optional.of(accessToken);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

}
