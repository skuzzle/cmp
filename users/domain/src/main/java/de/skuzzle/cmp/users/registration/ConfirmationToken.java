package de.skuzzle.cmp.users.registration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.google.common.base.Preconditions;

public final class ConfirmationToken {

    private final String token;
    private final LocalDateTime issuedAtUTC;
    private final long validForHours;

    private ConfirmationToken(String token, LocalDateTime issuedAtUTC, long validForHours) {
        this.token = token;
        this.issuedAtUTC = issuedAtUTC;
        this.validForHours = validForHours;
    }

    public static ConfirmationToken randomValidFor(Duration duration, LocalDateTime issuedAtUTC) {
        Preconditions.checkArgument(duration != null, "duration must not be null");
        Preconditions.checkArgument(issuedAtUTC != null, "issuedAtUTC must not be null");
        return new ConfirmationToken(UUID.randomUUID().toString(), issuedAtUTC, duration.toHours());
    }

    public static ConfirmationToken explicit(String token, Duration duration, LocalDateTime issuedAtUTC) {
        Preconditions.checkArgument(token != null, "token must not be null");
        Preconditions.checkArgument(duration != null, "duration must not be null");
        Preconditions.checkArgument(issuedAtUTC != null, "issuedAtUTC must not be null");
        return new ConfirmationToken(token, issuedAtUTC, duration.toHours());
    }

    public String token() {
        return this.token;
    }

    public boolean validate(String token, LocalDateTime nowUTC) {
        return matches(token) && !isExpiredAt(nowUTC);
    }

    public boolean matches(String token) {
        return this.token.equals(token);
    }

    public boolean isExpiredAt(LocalDateTime nowUTC) {
        return issuedAtUTC.plusHours(validForHours).isBefore(nowUTC);
    }

}
