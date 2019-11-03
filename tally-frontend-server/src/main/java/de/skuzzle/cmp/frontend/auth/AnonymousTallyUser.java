package de.skuzzle.cmp.frontend.auth;

class AnonymousTallyUser implements TallyUser {

    private static final TallyUser ONLY_INSTANCE = new AnonymousTallyUser();

    private AnonymousTallyUser() {
        // hidden
    }

    static TallyUser getInstance() {
        return ONLY_INSTANCE;
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
