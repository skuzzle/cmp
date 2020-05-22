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

    private static volatile Clock APPLICATION_CLOCK = Clock.systemUTC();

    public static Clock get() {
        if (logger.isDebugEnabled()) {
            final boolean clockChanged = !APPLICATION_CLOCK.equals(Clock.systemUTC());
            if (clockChanged) {
                logger.debug("Accessed global application clock which has been changed from UTC to {}",
                        APPLICATION_CLOCK);
            }
        }
        return APPLICATION_CLOCK;
    }

    public static void resetToDefaultClock() {
        final boolean clockChanged = !APPLICATION_CLOCK.equals(Clock.systemUTC());
        if (clockChanged) {
            APPLICATION_CLOCK = Clock.systemUTC();
            logger.info("Reset Application clock to UTC");
        }
    }

    public static void changeTo(Clock clock) {
        Preconditions.checkArgument(clock != null, "global ApplicationClock can not be null");
        if (clock.equals(APPLICATION_CLOCK)) {
            // early return to avoid warning log message
            return;
        }
        APPLICATION_CLOCK = clock;
        final boolean clockChanged = !APPLICATION_CLOCK.equals(Clock.systemUTC());
        if (clockChanged) {
            logger.warn("Application clock has been changed globally to {}", clock);
        }
    }

    /**
     * Returns the application's local time. That will be represented in UTC time zone,
     * unless the clock has been changed.
     *
     * @return The local time in UTC.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(APPLICATION_CLOCK);
    }
}
