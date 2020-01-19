package de.skuzzle.cmp.counter.domain;

public class TallySheetNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 8989339707034658628L;

    public TallySheetNotAvailableException(String message) {
        super(message);
    }
}
