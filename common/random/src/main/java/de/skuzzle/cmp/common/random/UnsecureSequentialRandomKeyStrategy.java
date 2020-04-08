package de.skuzzle.cmp.common.random;

import java.util.concurrent.atomic.AtomicInteger;

public final class UnsecureSequentialRandomKeyStrategy implements RandomKeyStrategy {

    public static final RandomKeyStrategy INSTANCE = new UnsecureSequentialRandomKeyStrategy();
    private final AtomicInteger idSequence = new AtomicInteger(0);
    private final AtomicInteger uuidSequence = new AtomicInteger(0);

    private UnsecureSequentialRandomKeyStrategy() {
        // hidden
    }

    @Override
    public String ofLength(int length) {
        final int id = idSequence.incrementAndGet();
        final String formatSequence = "%0" + length + "d";
        return String.format(formatSequence, id);
    }

    @Override
    public String randomUUID() {
        final int id = uuidSequence.incrementAndGet();
        final String formatSequence = "%032d";
        final String formattedId = String.format(formatSequence, id);
        return formattedId.substring(0, 9) + "-" +
                formattedId.substring(9, 13) + "-" +
                formattedId.substring(13, 17) + "-" +
                formattedId.substring(17, 21) + "-" +
                formattedId.substring(21);
    }

}
