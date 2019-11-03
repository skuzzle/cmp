package de.skuzzle.cmp.rest.ratelimit;

import java.util.Optional;

public interface ClientIdentificator<T> {

    Optional<ApiClient> tryIdentifyClientFrom(T hint);
}
