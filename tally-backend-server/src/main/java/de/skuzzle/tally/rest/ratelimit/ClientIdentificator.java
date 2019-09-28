package de.skuzzle.tally.rest.ratelimit;

public interface ClientIdentificator<T> {

    ApiClient identifyClientFrom(T hint);
}
