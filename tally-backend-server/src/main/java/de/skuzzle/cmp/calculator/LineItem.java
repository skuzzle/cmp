package de.skuzzle.cmp.calculator;

import com.google.common.base.Preconditions;

public class LineItem {

    private final String productName;
    private Amount amount = Amount.ONE;
    private Money singlePrice;
    private CalculatedPrices calculatedPrices;

    private LineItem(String productName, Money singlePrice) {
        Preconditions.checkArgument(productName != null, "productName must not be null");
        Preconditions.checkArgument(singlePrice != null, "singlePrice must not be null");
        this.singlePrice = singlePrice;
        this.productName = productName;
    }

    public static LineItem withNameAndPrice(String productName, Money singlePrice) {
        return new LineItem(productName, singlePrice);
    }

    public String getProductName() {
        return this.productName;
    }

    public LineItem withOriginalPrice(Money originalPrice) {
        CartTransaction.assertActiveTransaction();
        Preconditions.checkArgument(originalPrice != null, "originalPrice must not be null");
        this.singlePrice = originalPrice;
        return this;
    }

    public LineItem withAmount(Amount amount) {
        CartTransaction.assertActiveTransaction();
        Preconditions.checkArgument(amount != null, "amount must not be null");
        this.amount = amount;
        return this;
    }

    public Money getSinglePrice() {
        return this.singlePrice;
    }

    public Money getOriginalPrice() {
        return this.amount.times(singlePrice);
    }

    public CalculatedPrices getCalculatedPrices() {
        Preconditions.checkState(calculatedPrices != null, "prices have not been calculated yet on this LineItem: %s",
                this);
        return this.calculatedPrices;
    }

    CalculatedPrices updateCalculation(
            Money globalDiscount,
            Money totalOriginalPrice) {

        final Money originalPrice = getOriginalPrice();
        final Percentage percentageOfTotalPrice = originalPrice.inRelationTo(totalOriginalPrice);
        final Money discount = percentageOfTotalPrice.from(globalDiscount);
        final Percentage relativeDiscount = discount.inRelationTo(originalPrice);
        final Money discountedPrice = originalPrice.minus(discount);

        // todo: calculate real tip
        final Money tippedDiscountedPrice = discountedPrice;

        this.calculatedPrices = new CalculatedPrices(
                originalPrice,
                discountedPrice,
                discount,
                relativeDiscount,
                tippedDiscountedPrice,
                Money.ZERO,
                Percentage.ZERO);
        return this.calculatedPrices;
    }

    @Override
    public String toString() {
        return this.amount + " " + this.productName + ": " + this.singlePrice;
    }

    public String format(String prefix) {
        if (calculatedPrices != null) {
            return calculatedPrices.format(prefix + this.amount + " " + this.productName + "\t");
        }
        return prefix + this.productName + "\t" + this.singlePrice;
    }
}
