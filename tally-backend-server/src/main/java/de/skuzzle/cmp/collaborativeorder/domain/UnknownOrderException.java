package de.skuzzle.cmp.collaborativeorder.domain;

public class UnknownOrderException extends RuntimeException {

    private static final long serialVersionUID = -4114741375922406515L;

    public UnknownOrderException(String message) {
        super(message);
    }

}
