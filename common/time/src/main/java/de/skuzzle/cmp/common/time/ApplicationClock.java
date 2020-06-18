package de.skuzzle.cmp.common.time;

import java.time.Clock;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Provides the global application time. Allows to statically change the {@link Clock}
 * that is used throughout the whole application. Changing the clock should only be
 * necessary for unit testing purposes.
 *
 * @author Simon Taddiken
 */
public final class ApplicationClock {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationClock.class);

    private ApplicationClock() {
        // hidden
    }

    private static final Clock DEFAULT_CLOCK = Clock.systemUTC();
    private static volatile Clock APPLICATION_CLOCK = DEFAULT_CLOCK;

    public static Clock get() {
        if (logger.isDebugEnabled()) {
            final boolean clockChanged = !APPLICATION_CLOCK.equals(DEFAULT_CLOCK);
            if (clockChanged) {
                logger.debug("Accessed global application clock which has been changed from UTC to {}",
                        APPLICATION_CLOCK);
            }
        }
        return APPLICATION_CLOCK;
    }

    public static void resetToDefaultClock() {
        final boolean clockChanged = !APPLICATION_CLOCK.equals(DEFAULT_CLOCK);
        if (clockChanged) {
            APPLICATION_CLOCK = DEFAULT_CLOCK;
            logger.info("Reset Application clock to global default ({})", DEFAULT_CLOCK);
        }
    }

    public static void changeTo(Clock newClock) {
        Preconditions.checkArgument(newClock != null, "global ApplicationClock can not be null");
        final Clock currentClock = APPLICATION_CLOCK;
        if (newClock.equals(currentClock)) {
            // early return to avoid warning log message
            return;
        }
        APPLICATION_CLOCK = newClock;
        final boolean clockChanged = !newClock.equals(DEFAULT_CLOCK);
        if (clockChanged) {
            logger.warn("Application clock has been changed globally to {} (from default {})", newClock, DEFAULT_CLOCK);
        }
    }

    /**
     * Returns the application's local time. That will be represented in UTC time zone,
     * unless the clock has been changed.
     *
     * @return The local time in UTC.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(get());
    }
}
