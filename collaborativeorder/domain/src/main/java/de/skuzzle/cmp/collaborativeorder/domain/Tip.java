package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.Objects;

import com.google.common.base.Preconditions;

public final class Tip {

    public static final Tip NONE = Tip.absolute(Money.ZERO);

    private final Percentage percentage;
    private final Money absolute;

    private Tip(Percentage percentage, Money absolute) {
        Preconditions.checkArgument(percentage != null ^ absolute != null, "tip can either be relative OR absolute");
        this.percentage = percentage;
        this.absolute = absolute;
    }

    public static Tip absolute(Money absolute) {
        return new Tip(null, absolute);
    }

    public static Tip relative(Percentage percentage) {
        return new Tip(percentage, null);
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
        Preconditions.checkState(isAbsolute(), "tip is not absolute");
        return this.absolute;
    }

    Percentage getPercentage() {
        Preconditions.checkState(!isAbsolute(), "tip is not relative");
        return this.percentage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(absolute, percentage);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Tip
                && Objects.equals(absolute, ((Tip) obj).absolute)
                && Objects.equals(percentage, ((Tip) obj).percentage);
    }

    @Override
    public String toString() {
        if (absolute != null) {
            return absolute.toString();
        }
        return percentage.toString();
    }
}
