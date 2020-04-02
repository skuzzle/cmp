package de.skuzzle.cmp.users.registration;

public class ResetPasswordRequestEvent {

    private final String confirmationToken;
    private final String name;
    private final String email;

    private ResetPasswordRequestEvent(String name, String confirmationToken, String email) {
        this.name = name;
        this.confirmationToken = confirmationToken;
        this.email = email;
    }

    static ResetPasswordRequestEvent create(String name, String confirmationToken, String email) {
        return new ResetPasswordRequestEvent(name, confirmationToken, email);
    }

    public String name() {
        return this.name;
    }

    public String confirmationToken() {
        return this.confirmationToken;
    }

    public String email() {
        return this.email;
    }
}
