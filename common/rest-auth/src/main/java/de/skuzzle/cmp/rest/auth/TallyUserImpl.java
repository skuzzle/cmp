package de.skuzzle.cmp.rest.auth;

import java.util.Objects;

import com.google.common.base.Preconditions;

final class TallyUserImpl implements TallyUser {

    private final String source;
    private final String id;
    private final boolean anonymous;

    TallyUserImpl(String source, String id, boolean anonymous) {
        Preconditions.checkArgument(source != null, "source must not be null");
        Preconditions.checkArgument(id != null, "id must not be null");
        Preconditions.checkArgument(!anonymous || "unknown".equals(source));
        this.source = source;
        this.id = id;
        this.anonymous = anonymous;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isAnonymous() {
        return anonymous;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getId());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TallyUser
                && Objects.equals(getSource(), ((TallyUser) obj).getSource())
                && Objects.equals(getId(), ((TallyUserImpl) obj).getId());
    }

    @Override
    public String toString() {
        return String.format("%s:%s", getSource(), getId());
    }
}
