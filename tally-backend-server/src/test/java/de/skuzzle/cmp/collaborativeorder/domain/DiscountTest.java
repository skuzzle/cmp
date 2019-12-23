package de.skuzzle.cmp.collaborativeorder.domain;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.collaborativeorder.domain.Discount;
import nl.jqno.equalsverifier.EqualsVerifier;

public class DiscountTest {

    @Test
    void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Discount.class)
                .verify();
    }
}
