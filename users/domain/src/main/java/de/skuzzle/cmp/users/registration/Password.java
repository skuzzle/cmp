package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;

public class Password {

    private final String hashedPassword;
    private final LocalDateTime lastChangedUTC;

    private Password(String hashedPassword, LocalDateTime lastChangedUTC) {
        this.hashedPassword = hashedPassword;
        this.lastChangedUTC = lastChangedUTC;
    }

    public static Password create(String hashedPassword, LocalDateTime lastChangedUTC) {
        return new Password(hashedPassword, lastChangedUTC);
    }

    public String hashedPassword() {
        return this.hashedPassword;
    }

    public LocalDateTime lastChangedUTC() {
        return this.lastChangedUTC;
    }
}
