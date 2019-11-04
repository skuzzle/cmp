package de.skuzzle.cmp.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PercentageTest {

    @Test
    void testEquals() throws Exception {
        EqualsVerifier.forClass(Percentage.class)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    void testToString() throws Exception {
        final Percentage percentage = Percentage.percent(0.1);
        assertThat(percentage.toString()).isEqualTo("10.00%");
    }

    @Test
    void testToStringNoRound() throws Exception {
        final Percentage percentage = Percentage.percent(0.125);
        assertThat(percentage.toString()).isEqualTo("12.50%");
    }

    @Test
    void testToStringRound() throws Exception {
        final Percentage percentage = Percentage.percent(0.12505);
        assertThat(percentage.toString()).isEqualTo("12.51%");
    }
}
