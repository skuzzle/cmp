package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;

import com.google.common.base.Preconditions;

public class Password {

    private final String hashedPassword;
    private final LocalDateTime lastChangedUTC;

    private Password(String hashedPassword, LocalDateTime lastChangedUTC) {
        Preconditions.checkArgument(hashedPassword != null, "hashedPassword must not be null");
        Preconditions.checkArgument(lastChangedUTC != null, "lastChangedUTC must not be null");
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
