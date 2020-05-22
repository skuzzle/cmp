package de.skuzzle.cmp.auth;

import java.util.Optional;

class AnonymousTallyUser implements TallyUser {

    private static final TallyUser ONLY_INSTANCE = new AnonymousTallyUser();

    private AnonymousTallyUser() {
        // hidden
    }

    static TallyUser getInstance() {
        return ONLY_INSTANCE;
    }

    @Override
    public Optional<String> getAccessToken() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "unknown";
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
