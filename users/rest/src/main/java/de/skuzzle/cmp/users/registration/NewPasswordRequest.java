package de.skuzzle.cmp.users.registration;

public class NewPasswordRequest {

    private final CharSequence newRawPassword;

    public NewPasswordRequest(CharSequence newRawPassword) {
        this.newRawPassword = newRawPassword;
    }

    public CharSequence getNewRawPassword() {
        return this.newRawPassword;
    }
}
