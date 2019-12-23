package de.skuzzle.cmp.collaborativeorder.domain;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.collaborativeorder.domain.Tip;
import nl.jqno.equalsverifier.EqualsVerifier;

public class TipTest {

    @Test
    void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Tip.class)
                .verify();
    }
}
