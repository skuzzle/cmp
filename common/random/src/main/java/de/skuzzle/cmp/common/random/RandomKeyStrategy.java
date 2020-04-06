package de.skuzzle.cmp.common.random;

public interface RandomKeyStrategy {
    String ofLength(int length);

    String randomUUID();
}
