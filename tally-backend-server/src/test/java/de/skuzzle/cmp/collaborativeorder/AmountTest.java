package de.skuzzle.cmp.collaborativeorder;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AmountTest {

    @Test
    void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Amount.class).verify();
    }
}
