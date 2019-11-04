package de.skuzzle.cmp.calculator;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.calculator.Discount.AbsoluteDiscount;
import de.skuzzle.cmp.calculator.Discount.RelativeDiscount;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class DiscountTest {

    @Test
    void testEqualsRelativeDiscount() throws Exception {
        EqualsVerifier.forClass(RelativeDiscount.class)
                .suppress(Warning.ANNOTATION)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    void testEqualsAbsoluteDiscount() throws Exception {
        EqualsVerifier.forClass(AbsoluteDiscount.class)
                .suppress(Warning.ANNOTATION)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }
}
