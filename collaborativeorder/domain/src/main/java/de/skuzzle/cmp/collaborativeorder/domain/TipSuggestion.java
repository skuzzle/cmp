package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.Objects;

import com.google.common.base.Preconditions;

public final class TipSuggestion {

    private final Percentage percentage;
    private final Money minimum;

    private TipSuggestion(Percentage percentage, Money minimum) {
        Preconditions.checkArgument(percentage != null, "percentage must not be null");
        Preconditions.checkArgument(minimum != null, "minimum must not be null");
        this.percentage = percentage;
        this.minimum = minimum;
    }

    public static TipSuggestion relativeWithMinimum(Percentage percentage, Money minimum) {
        return new TipSuggestion(percentage, minimum);
    }

    public Money suggestTipFor(Money priceToPay) {
        return percentage.from(priceToPay).max(minimum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimum, percentage);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TipSuggestion
                && Objects.equals(minimum, ((TipSuggestion) obj).minimum)
                && Objects.equals(percentage, ((TipSuggestion) obj).percentage);
    }

    @Override
    public String toString() {
        return percentage.toString() + " but at least " + minimum;
    }
}
