package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;

import com.google.common.base.Preconditions;

public class Confirmation {

    private final ConfirmationToken confirmationToken;
    private final LocalDateTime confirmationTimeUTC;

    private Confirmation(ConfirmationToken confirmationToken, LocalDateTime confirmationTimeUTC) {
        this.confirmationToken = confirmationToken;
        this.confirmationTimeUTC = confirmationTimeUTC;
    }

    public static Confirmation unconfirmed(ConfirmationToken confirmationToken) {
        Preconditions.checkArgument(confirmationToken != null, "confirmationToken must not be null");
        return new Confirmation(confirmationToken, null);
    }

    public static Confirmation confirmed(LocalDateTime confirmationTimeUTC) {
        Preconditions.checkArgument(confirmationTimeUTC != null, "confirmationTimeUTC must not be null");
        return new Confirmation(null, confirmationTimeUTC);
    }

    public Confirmation confirm(String expectedToken, LocalDateTime confirmationTimeUTC) {
        Preconditions.checkState(!isConfirmed(), "already confirmed");
        Preconditions.checkArgument(confirmationTimeUTC != null, "confirmationTimeUTC must not be null");

        if (!confirmationToken.validate(expectedToken, confirmationTimeUTC)) {
            throw new ConfirmationFailedException("confirmation token invalid/expired");
        }

        return confirmed(confirmationTimeUTC);
    }

    public String tokenString() {
        Preconditions.checkState(!isConfirmed(), "already confirmed");
        return confirmationToken.token();
    }

    public boolean isConfirmed() {
        return this.confirmationToken == null;
    }

    public LocalDateTime confirmationTimeUTC() {
        return this.confirmationTimeUTC;
    }
}
