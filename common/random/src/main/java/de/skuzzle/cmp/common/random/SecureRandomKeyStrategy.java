package de.skuzzle.cmp.common.random;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;

public class SecureRandomKeyStrategy implements RandomKeyStrategy {

    public static final RandomKeyStrategy INSTANCE = new SecureRandomKeyStrategy();

    private static class Holder {
        private static final char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                .toCharArray();
        private static final SecureRandom random = new SecureRandom();
    }

    private SecureRandomKeyStrategy() {
        // hidden
    }

    @Override
    public String ofLength(int length) {
        Preconditions.checkArgument(length > 0, "Random key length must be > 0 but was %s", length);
        return IntStream.range(0, length)
                .map(i -> Holder.random.nextInt(Holder.chars.length))
                .map(idx -> Holder.chars[idx])
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }

    @Override
    public String randomUUID() {
        return UUID.randomUUID().toString();
    }

}
