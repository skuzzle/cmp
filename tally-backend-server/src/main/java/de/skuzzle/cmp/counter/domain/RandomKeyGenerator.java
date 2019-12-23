package de.skuzzle.cmp.counter.domain;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

@Component
class RandomKeyGenerator {

    private static final SecureRandom rnd = new SecureRandom();
    private static final char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public String generatePublicKey(int length) {
        return IntStream.range(0, length)
                .map(i -> rnd.nextInt(chars.length))
                .map(idx -> chars[idx])
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }

    public String generateAdminKey() {
        return UUID.randomUUID().toString();
    }
}
