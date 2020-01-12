package de.skuzzle.cmp.common.ratelimit;

import java.util.Objects;

import com.google.common.base.Preconditions;

public class BaseApiClient {

    protected final Object key;

    protected BaseApiClient(Object key) {
        Preconditions.checkArgument(key != null, "key for identifying a client must not be null");
        this.key = key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof SimpleApiClient
                && Objects.equals(key, ((SimpleApiClient) obj).key);
    }

    @Override
    public String toString() {
        return "ApiClient: " + key;
    }

}