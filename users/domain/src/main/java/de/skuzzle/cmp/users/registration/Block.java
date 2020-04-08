package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;

public class Block {

    private final LocalDateTime blockedSinceUTC;
    private final LocalDateTime blockedUntilUTC;
    private final String reason;

    private Block(LocalDateTime blockedSinceUTC, LocalDateTime blockedUntilUTC, String reason) {
        this.blockedSinceUTC = blockedSinceUTC;
        this.blockedUntilUTC = blockedUntilUTC;
        this.reason = reason;
    }

    public static Block until(LocalDateTime blockedUntilUTC, LocalDateTime nowUTC, String reason) {
        return new Block(nowUTC, blockedUntilUTC, reason);
    }

    public boolean isBlocked(LocalDateTime nowUTC) {
        return nowUTC.isBefore(blockedUntilUTC);
    }
}
