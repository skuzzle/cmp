package de.skuzzle.cmp.calculator;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.calculator.Tip.AbsoluteTip;
import de.skuzzle.cmp.calculator.Tip.RelativeTip;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class TipTest {

    @Test
    void testEqualsRelativeTip() throws Exception {
        EqualsVerifier.forClass(RelativeTip.class)
                .suppress(Warning.ANNOTATION)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    void testEqualsAbsoluteTip() throws Exception {
        EqualsVerifier.forClass(AbsoluteTip.class)
                .suppress(Warning.ANNOTATION)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }
}
