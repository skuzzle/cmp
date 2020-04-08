package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;

import com.google.common.base.Preconditions;

public class LoginRequest {

    private final LocalDateTime loginRequestTimeUTC;
    private final String email;
    private final CharSequence rawPassword;

    private LoginRequest(LocalDateTime loginRequestTimeUTC, String email, CharSequence rawPassword) {
        this.loginRequestTimeUTC = loginRequestTimeUTC;
        this.email = email;
        this.rawPassword = rawPassword;
    }

    public static LoginRequest create(LocalDateTime loginRequestTimeUTC, String email, CharSequence rawPassword) {
        Preconditions.checkArgument(loginRequestTimeUTC != null, "loginRequestTimeUTC must not be null");
        Preconditions.checkArgument(email != null, "email must not be null");
        Preconditions.checkArgument(rawPassword != null, "rawPassword must not be null");
        return new LoginRequest(loginRequestTimeUTC, email, rawPassword);
    }

    public LocalDateTime loginRequestTimeUTC() {
        return this.loginRequestTimeUTC;
    }

    public String email() {
        return this.email;
    }

    public CharSequence rawPassword() {
        return this.rawPassword;
    }
}
