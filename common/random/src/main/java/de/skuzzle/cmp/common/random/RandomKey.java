package de.skuzzle.cmp.common.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public final class RandomKey {

    private static final Logger log = LoggerFactory.getLogger(RandomKey.class);

    private static volatile RandomKeyStrategy STRATEGY = SecureRandomKeyStrategy.INSTANCE;
    private static final int DEFAULT_KEY_LENGTH = 8;

    private RandomKey() {
        // hidden
    }

    public static String ofDefaultLength() {
        return ofLength(DEFAULT_KEY_LENGTH);
    }

    public static String ofLength(int length) {
        return STRATEGY.ofLength(length);
    }

    public static String randomUUID() {
        return STRATEGY.randomUUID();
    }

    public static void replaceGlobalStrategyWith(RandomKeyStrategy randomKeyStrategy) {
        Preconditions.checkArgument(randomKeyStrategy != null, "randomKeyStrategy must not be null");
        log.warn("Replacing global RandomKeyStrategy from {} to {}", STRATEGY, randomKeyStrategy);
        STRATEGY = randomKeyStrategy;
    }

}
