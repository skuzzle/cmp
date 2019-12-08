package de.skuzzle.cmp.collaborativeorder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class MoneyTest {

    @Test
    void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Money.class)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    void testPlusPercentage() throws Exception {
        assertThat(Money.money(100).plus(Percentage.percent(0.5))).isEqualTo(Money.money(150));
    }

    @Test
    void testToStringNoDecimals() throws Exception {
        final Money money = Money.money(10);
        assertThat(money.toString()).isEqualTo("10.00");
    }

    @Test
    void testToStringWithDecimals() throws Exception {
        final Money money = Money.money(10.02);
        assertThat(money.toString()).isEqualTo("10.02");
    }

    @Test
    void testToStringRound() throws Exception {
        final Money money = Money.money(10.025);
        assertThat(money.toString()).isEqualTo("10.03");
    }

    @Test
    void testToStringBigAmountWithDecimals() throws Exception {
        final Money money = Money.money(10000.02);
        assertThat(money.toString()).isEqualTo("10000.02");
    }

    @Test
    void testToStringBigAmountWithDecimalsAndRound() throws Exception {
        final Money money = Money.money(10000.025);
        assertThat(money.toString()).isEqualTo("10000.03");
    }

}
