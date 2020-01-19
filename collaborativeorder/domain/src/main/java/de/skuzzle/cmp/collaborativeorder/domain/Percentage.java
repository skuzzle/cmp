package de.skuzzle.cmp.collaborativeorder.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.google.common.base.Preconditions;

public final class Percentage implements Comparable<Percentage> {

    public static final Percentage ZERO = percent(0.0);
    public static final Percentage HOUNDRED = percent(1.0);

    private static final MathContext CTX = MathContext.DECIMAL32;

    // only for converting to "human readable" values in range 0-100
    private static final BigDecimal BD_HOUNDRED = BigDecimal.valueOf(100L);

    // should be between 0 and 1 (inclusive), but other values are still valid to
    // represent percentages like 120%
    private final BigDecimal percentage;

    private Percentage(BigDecimal percentage) {
        Preconditions.checkArgument(percentage != null, "percentage must not be null");
        this.percentage = percentage;
    }

    public static Percentage percent(BigDecimal percentage) {
        return new Percentage(percentage);
    }

    public static Percentage percent(double percent) {
        return new Percentage(BigDecimal.valueOf(percent));
    }

    public Money from(Money money) {
        return money.multiply(percentage);
    }

    public Money addTo(Money money) {
        return money.multiply(HOUNDRED.percentage.add(percentage, CTX));
    }

    public Percentage complementary() {
        return new Percentage(HOUNDRED.percentage.subtract(percentage, CTX));
    }

    BigDecimal getPercentage() {
        return this.percentage;
    }

    @Override
    public int hashCode() {
        return rounded().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Percentage
                && compareTo(((Percentage) obj)) == 0;
    }

    private BigDecimal rounded() {
        return percentage.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int compareTo(Percentage o) {
        return rounded().compareTo(o.rounded());
    }

    @Override
    public String toString() {
        final String value = percentage.multiply(BD_HOUNDRED).setScale(2, RoundingMode.HALF_UP).toPlainString();
        return value + "%";
    }
}
