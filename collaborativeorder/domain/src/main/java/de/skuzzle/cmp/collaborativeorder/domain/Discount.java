package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.Objects;

import com.google.common.base.Preconditions;

public final class Discount {

    public static final Discount NONE = absolute(Money.ZERO);

    private final Percentage percentage;
    private final Money absolute;

    private Discount(Percentage percentage, Money absolute) {
        Preconditions.checkArgument(percentage != null ^ absolute != null,
                "discount can either be relative OR absolute");
        this.percentage = percentage;
        this.absolute = absolute;
    }

    public static Discount absolute(Money absolute) {
        return new Discount(null, absolute);
    }

    public static Discount relative(Percentage percentage) {
        return new Discount(percentage, null);
    }

    public Money getAbsoluteValue(Money money) {
        if (absolute != null) {
            return absolute;
        } else {
            return this.percentage.from(money);
        }
    }

    boolean isAbsolute() {
        return absolute != null;
    }

    Money getAbsolute() {
        Preconditions.checkState(isAbsolute(), "discount is not absolute");
        return this.absolute;
    }

    Percentage getPercentage() {
        Preconditions.checkState(!isAbsolute(), "discount is not relative");
        return this.percentage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(absolute, percentage);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Discount
                && Objects.equals(absolute, ((Discount) obj).absolute)
                && Objects.equals(percentage, ((Discount) obj).percentage);
    }

    @Override
    public String toString() {
        if (absolute != null) {
            return absolute.toString();
        }
        return percentage.toString();
    }
}
