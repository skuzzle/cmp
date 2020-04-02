package de.skuzzle.cmp.users.registration;

public class UserRegisteredEvent {

    private final String name;
    private final String confirmationToken;
    private final String email;

    private UserRegisteredEvent(String name, String confirmationToken, String email) {
        this.name = name;
        this.confirmationToken = confirmationToken;
        this.email = email;
    }

    static UserRegisteredEvent create(String name, String confirmationToken, String email) {
        return new UserRegisteredEvent(name, confirmationToken, email);
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
