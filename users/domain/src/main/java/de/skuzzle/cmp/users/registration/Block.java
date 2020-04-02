package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;

import com.google.common.base.Preconditions;

public final class Block {

    private static final Block NOT_BLOCKED = new Block(null, null, "");

    private final LocalDateTime blockedSinceUTC;
    private final LocalDateTime blockedUntilUTC;
    private final String reason;

    private Block(LocalDateTime blockedSinceUTC, LocalDateTime blockedUntilUTC, String reason) {
        this.blockedSinceUTC = blockedSinceUTC;
        this.blockedUntilUTC = blockedUntilUTC;
        this.reason = reason;
    }

    public static Block notBlocked() {
        return NOT_BLOCKED;
    }

    public static Block until(LocalDateTime blockedUntilUTC, LocalDateTime nowUTC, String reason) {
        Preconditions.checkArgument(nowUTC != null, "nowUTC must not be null");
        Preconditions.checkArgument(blockedUntilUTC != null, "blockedUntilUTC must not be null");
        Preconditions.checkArgument(reason != null, "reason must not be null");
        return new Block(nowUTC, blockedUntilUTC, reason);
    }

    public boolean isBlocked(LocalDateTime nowUTC) {
        return blockedUntilUTC != null && nowUTC.isBefore(blockedUntilUTC);
    }

}
