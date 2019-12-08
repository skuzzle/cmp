package de.skuzzle.cmp.collaborativeorder;

public final class TipSuggestion {

    private final Percentage percentage;
    private final Money minimum;

    public TipSuggestion(Percentage percentage, Money minimum) {
        this.percentage = percentage;
        this.minimum = minimum;
    }

    public Money suggestTipFor(Money priceToPay) {
        return percentage.from(priceToPay).max(minimum);
    }
}
