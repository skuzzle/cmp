package de.skuzzle.cmp.calculator;

import com.google.common.base.Preconditions;

public class LineItem {

    private final String productName;
    private String category;
    private Money originalPrice;
    private CalculatedPrices calculatedPrices;

    private LineItem(String productName, Money originalPrice) {
        Preconditions.checkArgument(productName != null, "productName must not be null");
        Preconditions.checkArgument(originalPrice != null, "originalPrice must not be null");
        this.originalPrice = originalPrice;
        this.productName = productName;
        this.category = "";
    }

    public static LineItem withNameAndPrice(String productName, Money originalPrice) {
        return new LineItem(productName, originalPrice);
    }

    public String getProductName() {
        return this.productName;
    }

    public LineItem setCategory(String category) {
        Preconditions.checkArgument(category != null, "category must not be null");
        this.category = category;
        return this;
    }

    public LineItem withOriginalPrice(Money originalPrice) {
        CartTransaction.assertActiveTransaction();
        Preconditions.checkArgument(originalPrice != null, "originalPrice must not be null");
        this.originalPrice = originalPrice;
        return this;
    }

    public Money getOriginalPrice() {
        return this.originalPrice;
    }

    public String getCategory() {
        return this.category;
    }

    public CalculatedPrices getCalculatedPrices() {
        Preconditions.checkState(calculatedPrices != null, "prices have not been calculated yet on this LineItem: %s",
                this);
        return this.calculatedPrices;
    }

    CalculatedPrices updateCalculation(
            Money globalDiscount,
            Money totalOriginalPrice) {

        final Percentage percentageOfTotalPrice = this.originalPrice.inRelationTo(totalOriginalPrice);
        final Money discount = percentageOfTotalPrice.from(globalDiscount);
        final Percentage relativeDiscount = discount.inRelationTo(this.originalPrice);
        final Money discountedPrice = this.originalPrice.minus(discount);

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

    public String format(String prefix) {
        if (calculatedPrices != null) {
            return calculatedPrices.format(prefix + this.productName + "\t");
        }
        return prefix + this.productName + "\t" + this.originalPrice;
    }
}
