package de.skuzzle.cmp.users.registration;

public class RegisterUserRequest {

    private final String name;
    private final String email;
    private final CharSequence rawPassword;

    public RegisterUserRequest(String name, String email, CharSequence rawPassword) {
        this.name = name;
        this.email = email;
        this.rawPassword = rawPassword;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public CharSequence getRawPassword() {
        return this.rawPassword;
    }

}
