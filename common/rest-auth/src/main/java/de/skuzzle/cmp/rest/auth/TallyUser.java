package de.skuzzle.cmp.rest.auth;

public interface TallyUser {

    public static TallyUser create(String source, String id, boolean anonymous) {
        return new TallyUserImpl(source, id, anonymous);
    }

    public static TallyUser unknownWithId(String id) {
        return new TallyUserImpl("unknown", id, true);
    }

    public static TallyUser forTestWithId(String id) {
        return new TallyUserImpl("test", id, false);
    }

    String getSource();

    String getId();

    boolean isAnonymous();

}