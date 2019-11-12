package de.skuzzle.cmp.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.google.common.base.Preconditions;

public final class Money implements Comparable<Money> {

    private static final MathContext CTX = MathContext.DECIMAL32;
    public static final Money ZERO = money(0.0);

    private final BigDecimal monetaryAmount;

    private Money(BigDecimal monetaryAmount) {
        Preconditions.checkArgument(monetaryAmount != null, "amount must not be null");
        this.monetaryAmount = monetaryAmount;
    }

    public static Money money(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Percentage inRelationTo(Money other) {
        return Percentage.percent(monetaryAmount.divide(other.monetaryAmount, CTX));
    }

    public Money max(Money other) {
        return this.compareTo(other) > 0
                ? this
                : other;
    }

    public Money min(Money other) {
        return this.compareTo(other) < 0
                ? this
                : other;
    }

    public Money plus(Percentage percentage) {
        return percentage.addHoundred().from(this);
    }

    public Money plus(Money other) {
        return new Money(monetaryAmount.add(other.monetaryAmount, CTX));
    }

    public Money minus(Money other) {
        return new Money(monetaryAmount.subtract(other.monetaryAmount, CTX));
    }

    public Money multiply(BigDecimal bigDecimal) {
        return new Money(monetaryAmount.multiply(bigDecimal, CTX));
    }

    @Override
    public String toString() {
        return rounded().toPlainString();
    }

    @Override
    public int hashCode() {
        return rounded().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Money
                && compareTo(((Money) obj)) == 0;
    }

    private BigDecimal rounded() {
        return monetaryAmount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int compareTo(Money o) {
        return rounded().compareTo(o.rounded());
    }

}
