package de.skuzzle.cmp.calculator;

import com.google.common.base.Preconditions;

public abstract class Discount {

    private static final Discount ZERO = absolute(Money.ZERO);

    public static Discount none() {
        return ZERO;
    }

    public static Discount absolute(Money absolute) {
        return new AbsoluteDiscount(absolute);
    }

    public static Discount relative(Percentage percentage) {
        return new RelativeDiscount(percentage);
    }

    public abstract Money getAbsoluteValue(Money total);

    static final class AbsoluteDiscount extends Discount {
        private final Money discount;

        AbsoluteDiscount(Money discount) {
            Preconditions.checkArgument(discount != null, "absolute discount value must not be null");
            this.discount = discount;
        }

        @Override
        public Money getAbsoluteValue(Money total) {
            return discount;
        }

        @Override
        public int hashCode() {
            return discount.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof AbsoluteDiscount
                    && discount.equals(((AbsoluteDiscount) obj).discount);
        }

        @Override
        public String toString() {
            return discount.toString();
        }
    }

    static final class RelativeDiscount extends Discount {
        private final Percentage percentage;

        RelativeDiscount(Percentage percentage) {
            Preconditions.checkArgument(percentage != null, "relative discount must not be null");
            this.percentage = percentage;
        }

        @Override
        public Money getAbsoluteValue(Money total) {
            return percentage.from(total);
        }

        @Override
        public int hashCode() {
            return percentage.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof RelativeDiscount
                    && percentage.equals(((RelativeDiscount) obj).percentage);
        }

        @Override
        public String toString() {
            return percentage.toString();
        }
    }
}
