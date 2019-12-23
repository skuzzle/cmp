package de.skuzzle.cmp.counter.domain;

public class IncrementNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 8989339707034658628L;

    public IncrementNotAvailableException(String message) {
        super(message);
    }
}
