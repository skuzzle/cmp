package de.skuzzle.cmp.collaborativeorder.domain;

import org.springframework.data.annotation.Transient;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.common.table.ConsoleTable;
import de.skuzzle.cmp.common.table.RowData;

public class LineItem {

    private final Money singlePrice;
    private final String productName;
    private final Amount amount;

    @Transient
    private CalculatedPrices calculatedPrices;

    private LineItem(String productName, Amount amount, Money singlePrice) {
        Preconditions.checkArgument(productName != null, "productName must not be null");
        Preconditions.checkArgument(amount != null, "amount must not be null");
        Preconditions.checkArgument(!amount.equals(Amount.ZERO), "amount must not be 0");
        Preconditions.checkArgument(singlePrice != null, "singlePrice must not be null");

        this.singlePrice = singlePrice;
        this.amount = amount;
        this.productName = productName;

        this.calculatedPrices = CalculatedPrices.ZERO;
    }

    public static LineItem singleProductWithName(String productName, Money singlePrice) {
        return new LineItem(productName, Amount.ONE, singlePrice);
    }

    public static LineItem multipleProductsWithName(String productName, Money singlePrice, Amount amount) {
        return new LineItem(productName, amount, singlePrice);
    }

    public String getProductName() {
        return this.productName;
    }

    public Money getSinglePrice() {
        return this.singlePrice;
    }

    public Amount getAmount() {
        return this.amount;
    }

    public Money getOriginalPrice() {
        return amount.times(singlePrice);
    }

    public CalculatedPrices getCalculatedPrices() {
        return this.calculatedPrices;
    }

    CalculatedPrices updateCalculation(
            Money absoluteGlobalDiscount,
            Money totalSumOfItemPrices) {

        final Money originalPrice = getOriginalPrice();
        final Percentage percentageOfTotalPrice = originalPrice.inRelationTo(totalSumOfItemPrices);
        final Money discount = percentageOfTotalPrice.from(absoluteGlobalDiscount);
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

    void toTable(ConsoleTable table) {
         table.addRow(RowData.of("    " + productName, 
                 amount, 
                 singlePrice, 
                 calculatedPrices.getOriginalPrice(),
                 calculatedPrices.getRelativeDiscount(),
                 calculatedPrices.getAbsoluteDiscount(),
                 calculatedPrices.getDiscountedPrice(), 
                 calculatedPrices.getRelativeTip(),
                 calculatedPrices.getAbsoluteTip(),
                 calculatedPrices.getTippedDiscountedPrice()));
    }
}
