package de.skuzzle.cmp.users.registration;

public class RegisterFailedException extends RuntimeException {

    private static final long serialVersionUID = -343451711823027835L;

    public RegisterFailedException(String message) {
        super(message);
    }

}
