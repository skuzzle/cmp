package de.skuzzle.cmp.users.registration;

public class ResetPasswordRequest {

    private final String email;

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
