package de.skuzzle.cmp.collaborativeorder.domain;

public class RestUserId {

    private final String source;
    private final String id;
    private final boolean anonymous;

    public RestUserId(String source, String id, boolean anonymous) {
        this.source = source;
        this.id = id;
        this.anonymous = anonymous;
    }

    public String getId() {
        return this.id;
    }

    public String getSource() {
        return this.source;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }
}
