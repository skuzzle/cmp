package de.skuzzle.cmp.counter.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.counter.domain.TallyIncrement;

public class TallyIncrementTest {

    @Test
    void testTagListIsImmutable() throws Exception {
        final TallyIncrement increment = TallyIncrement.newIncrement("foo", LocalDateTime.now(), Set.of());
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(
                () -> increment.getTags().add("string"));
    }
}
