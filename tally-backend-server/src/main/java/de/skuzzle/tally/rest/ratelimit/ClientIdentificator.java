package de.skuzzle.tally.rest.ratelimit;

import java.util.Optional;

public interface ClientIdentificator<T> {

    Optional<ApiClient> tryIdentifyClientFrom(T hint);
}
