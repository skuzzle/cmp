package de.skuzzle.tally.rest.ratelimit;

import java.util.Objects;

public class BaseApiClient {

    protected final Object key;

    protected BaseApiClient(Object key) {
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