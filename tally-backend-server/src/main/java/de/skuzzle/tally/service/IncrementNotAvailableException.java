package de.skuzzle.tally.service;

public class IncrementNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 8989339707034658628L;

    public IncrementNotAvailableException(String message) {
        super(message);
    }
}
