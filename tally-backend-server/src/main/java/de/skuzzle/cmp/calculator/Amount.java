package de.skuzzle.cmp.calculator;

import java.math.BigDecimal;

public final class Amount implements Comparable<Amount> {

    public static final Amount ZERO = times(0);
    public static final Amount ONE = times(1);

    private final int amount;

    private Amount(int amount) {
        this.amount = amount;
    }

    public static Amount times(int amount) {
        return new Amount(amount);
    }

    public Money times(Money money) {
        return money.multiply(BigDecimal.valueOf(amount));
    }

    @Override
    public int hashCode() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Amount
                && amount == ((Amount) obj).amount;
    }

    @Override
    public String toString() {
        return amount + "x";
    }

    @Override
    public int compareTo(Amount o) {
        return Integer.compare(amount, o.amount);
    }
}
