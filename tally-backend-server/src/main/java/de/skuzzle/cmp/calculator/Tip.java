package de.skuzzle.cmp.calculator;

import com.google.common.base.Preconditions;

public abstract class Tip {

    private static final Tip ZERO = absolute(Money.ZERO);

    public static Tip none() {
        return ZERO;
    }

    public static Tip absolute(Money tip) {
        return new AbsoluteTip(tip);
    }

    public static Tip relative(Percentage percentage) {
        return new RelativeTip(percentage);
    }

    public abstract Money getAbsoluteValue(Money total);

    static final class AbsoluteTip extends Tip {
        private final Money tip;

        AbsoluteTip(Money tip) {
            Preconditions.checkArgument(tip != null, "Absolute tip value must not be null");
            this.tip = tip;
        }

        @Override
        public Money getAbsoluteValue(Money total) {
            return this.tip;
        }

        @Override
        public int hashCode() {
            return this.tip.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof AbsoluteTip
                    && tip.equals(((AbsoluteTip) obj).tip);
        }

        @Override
        public String toString() {
            return tip.toString();
        }
    }

    static final class RelativeTip extends Tip {
        private final Percentage percentage;

        RelativeTip(Percentage percentage) {
            Preconditions.checkArgument(percentage != null, "Relative tip value must not be null");
            this.percentage = percentage;
        }

        @Override
        public Money getAbsoluteValue(Money total) {
            return percentage.from(total);
        }

        @Override
        public int hashCode() {
            return this.percentage.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof RelativeTip
                    && percentage.equals(((RelativeTip) obj).percentage);
        }

        @Override
        public String toString() {
            return percentage.toString();
        }
    }
}
