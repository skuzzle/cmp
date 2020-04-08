package de.skuzzle.cmp.counter.domain;

public class ShareNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = -4290971291665237148L;

    ShareNotAvailableException(String message) {
        super(message);
    }
}
