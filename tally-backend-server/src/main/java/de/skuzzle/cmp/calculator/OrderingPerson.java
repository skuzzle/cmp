package de.skuzzle.cmp.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Preconditions;

public class OrderingPerson {

    private final String id;
    private final String name;
    private final List<LineItem> lineItems;
    private Tip tip = Tip.none();
    private CalculatedPrices calculatedPrices;

    public OrderingPerson(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.lineItems = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<LineItem> getLineItems() {
        return this.lineItems;
    }

    public OrderingPerson addLineItem(LineItem lineItem) {
        Preconditions.checkArgument(lineItem != null, "lineItem must not be null");
        CartTransaction.assertActiveTransaction();

        this.lineItems.add(lineItem);
        return this;
    }

    public CalculatedPrices getCalculatedPrices() {
        Preconditions.checkState(calculatedPrices != null,
                "prices have not been calculated yet on this OrderingPerson: %s", this);
        return this.calculatedPrices;
    }

    CalculatedPrices updateCalculation(Money absoluteGlobalDiscount, Money totalOriginalPrice) {
        final Money originalPrice = calculateOriginalPrice();

        final Money discountedPrice = lineItems.stream()
                .map(li -> li.updateCalculation(absoluteGlobalDiscount, totalOriginalPrice))
                .map(CalculatedPrices::getDiscountedPrice)
                .reduce(Money.ZERO, Money::plus);

        final Money absoluteTip = tip.getAbsoluteValue(discountedPrice);
        final Percentage relativeDiscount = discountedPrice.inRelationTo(originalPrice).complementary();
        final Money absoluteDiscount = originalPrice.minus(discountedPrice);
        final Percentage relativeTip = absoluteTip.inRelationTo(discountedPrice);
        final Money tippedDiscountedPrice = discountedPrice.plus(absoluteTip);

        this.calculatedPrices = new CalculatedPrices(
                originalPrice,
                discountedPrice,
                absoluteDiscount,
                relativeDiscount,
                tippedDiscountedPrice,
                absoluteTip, relativeTip);
        return calculatedPrices;
    }

    public Money calculateOriginalPrice() {
        return lineItems.stream()
                .map(LineItem::getOriginalPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    public OrderingPerson payTip(Tip tip) {
        Preconditions.checkArgument(tip != null, "tip must not be null");
        CartTransaction.assertActiveTransaction();

        this.tip = tip;
        return this;
    }

    OrderingPerson suggestTip(TipSuggestion tipSuggestion) {
        CartTransaction.assertActiveTransaction();

        final Money discountedPrice = this.calculatedPrices.getDiscountedPrice();
        final Money suggestedTip = tipSuggestion.suggestTipFor(discountedPrice);

        this.tip = Tip.absolute(suggestedTip);
        return this;
    }

    public String format(String prefix) {
        final StringBuilder stringBuilder = new StringBuilder()
                .append(prefix)
                .append(this.name)
                .append(this.calculatedPrices.format("\t\t\t\t"))
                .append("\n");

        for (final LineItem lineItem : this.lineItems) {
            stringBuilder.append(lineItem.format(prefix + "\t")).append("\n");
        }
        return stringBuilder.toString();
    }
}
