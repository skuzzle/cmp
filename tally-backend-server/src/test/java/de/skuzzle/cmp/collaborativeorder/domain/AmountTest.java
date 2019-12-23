package de.skuzzle.cmp.collaborativeorder.domain;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.collaborativeorder.domain.Amount;
import nl.jqno.equalsverifier.EqualsVerifier;

public class AmountTest {

    @Test
    void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Amount.class).verify();
    }
}
