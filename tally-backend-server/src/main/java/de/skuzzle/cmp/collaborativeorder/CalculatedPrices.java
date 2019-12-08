package de.skuzzle.cmp.collaborativeorder;

import com.google.common.base.Preconditions;

final class CalculatedPrices {

    private static final boolean ASSERT_CONSISTENCY = true;
    public static final CalculatedPrices ZERO = new CalculatedPrices(
            Money.ZERO,
            Money.ZERO,
            Money.ZERO,
            Percentage.ZERO,
            Money.ZERO,
            Money.ZERO,
            Percentage.ZERO);

    private final Money originalPrice;
    private final Money discountedPrice;
    private final Money absoluteDiscount;
    private final Percentage relativeDiscount;
    private final Money tippedDiscountedPrice;
    private final Money absoluteTip;
    private final Percentage relativeTip;

    public CalculatedPrices(Money originalPrice,
            Money discountedPrice,
            Money absoluteDiscount,
            Percentage relativeDiscount,
            Money tippedDiscountedPrice,
            Money absoluteTip,
            Percentage relativeTip) {

        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.absoluteDiscount = absoluteDiscount;
        this.relativeDiscount = relativeDiscount;
        this.tippedDiscountedPrice = tippedDiscountedPrice;
        this.relativeTip = relativeTip;
        this.absoluteTip = absoluteTip;

        if (ASSERT_CONSISTENCY) {
            checkConsistency();
        }

    }

    void checkConsistency() {
        Preconditions.checkArgument(
                getAbsoluteDiscount().plus(getDiscountedPrice()).equals(getOriginalPrice()),
                "Discount + DiscountedPrice should add up to OriginalPrice");

        Preconditions.checkArgument(
                getRelativeDiscount().complementary().from(getOriginalPrice()).equals(getDiscountedPrice()),
                "Relative discount from OriginalPrice should give calculated DiscountedPrice");

        Preconditions.checkArgument(
                getAbsoluteTip().plus(getDiscountedPrice()).equals(getTippedDiscountedPrice()),
                "AbsoluteTip + DiscountedPrice should give TippedDiscountedPrice");

        Preconditions.checkArgument(
                getDiscountedPrice().plus(getRelativeDiscount()).equals(getTippedDiscountedPrice()),
                "RelativeTip + DiscountedPrice should give TippedDiscountedPrice");
    }

    public Money getOriginalPrice() {
        return this.originalPrice;
    }

    public Money getDiscountedPrice() {
        return this.discountedPrice;
    }

    public Money getAbsoluteDiscount() {
        return this.absoluteDiscount;
    }

    public Percentage getRelativeDiscount() {
        return this.relativeDiscount;
    }

    public Money getTippedDiscountedPrice() {
        return this.tippedDiscountedPrice;
    }

    public Money getAbsoluteTip() {
        return this.absoluteTip;
    }

    public Percentage getRelativeTip() {
        return this.relativeTip;
    }

    public String format(String prefix) {
        return new StringBuilder()
                .append(prefix)
                .append(originalPrice)
                .append("\t")
                .append(absoluteDiscount)
                .append("\t")
                .append(relativeDiscount)
                .append("\t")
                .append(discountedPrice)
                .append("\t")
                .append(dashForZero(absoluteTip))
                .append("\t")
                .append(dashForZero(relativeTip))
                .append("\t")
                .append(tippedDiscountedPrice)
                .toString();
    }

    private String dashForZero(Money money) {
        return Money.ZERO.equals(money)
                ? "  "
                : money.toString();
    }

    private String dashForZero(Percentage percentage) {
        return Percentage.ZERO.equals(percentage)
                ? "  "
                : percentage.toString();
    }
}
