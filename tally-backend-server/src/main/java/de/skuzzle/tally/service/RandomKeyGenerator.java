package de.skuzzle.tally.service;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
class RandomKeyGenerator {

    private static final SecureRandom rnd = new SecureRandom();
    private static final char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public String generatePublicKey(int length) {
        final StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final int idx = rnd.nextInt(chars.length);
            final char c = chars[idx];
            result.append(c);
        }
        return result.toString();
    }

    public String generateAdminKey() {
        return UUID.randomUUID().toString();
    }
}
