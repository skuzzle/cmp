package de.skuzzle.cmp.users.registration;

public class ConfirmationFailedException extends RuntimeException {

    private static final long serialVersionUID = -3653064106802841167L;

    public ConfirmationFailedException(String message) {
        super(message);
    }
}
