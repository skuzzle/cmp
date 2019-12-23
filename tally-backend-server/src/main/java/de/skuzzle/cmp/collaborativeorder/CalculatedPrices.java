package de.skuzzle.cmp.collaborativeorder;

import com.google.common.base.Preconditions;

final class CalculatedPrices {

    private static final boolean ASSERT_CONSISTENCY_ON_CONSTRUCTION = true;
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

        Preconditions.checkArgument(originalPrice != null, "originalPrice must not be null");
        Preconditions.checkArgument(discountedPrice != null, "discountedPrice must not be null");
        Preconditions.checkArgument(absoluteDiscount != null, "absoluteDiscount must not be null");
        Preconditions.checkArgument(relativeDiscount != null, "relativeDiscount must not be null");
        Preconditions.checkArgument(tippedDiscountedPrice != null, "tippedDiscountedPrice must not be null");
        Preconditions.checkArgument(absoluteTip != null, "absoluteTip must not be null");
        Preconditions.checkArgument(relativeTip != null, "relativeTip must not be null");
        
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.absoluteDiscount = absoluteDiscount;
        this.relativeDiscount = relativeDiscount;
        this.tippedDiscountedPrice = tippedDiscountedPrice;
        this.relativeTip = relativeTip;
        this.absoluteTip = absoluteTip;

        if (ASSERT_CONSISTENCY_ON_CONSTRUCTION) {
            checkConsistency();
        }

    }

    void checkConsistency() {
        Preconditions.checkArgument(
                getAbsoluteDiscount().plus(getDiscountedPrice()).equals(getOriginalPrice()),
                "Discount (%s) + DiscountedPrice (%s) should add up to OriginalPrice (%s)",
                getAbsoluteDiscount(), getDiscountedPrice(), getOriginalPrice());

        Preconditions.checkArgument(
                getRelativeDiscount().complementary().from(getOriginalPrice()).equals(getDiscountedPrice()),
                "RelativeDiscount (%s) from OriginalPrice (%s) should give CalculatedDiscountedPrice (%s)",
                getRelativeDiscount(), getOriginalPrice(), getDiscountedPrice());

        Preconditions.checkArgument(
                getAbsoluteTip().plus(getDiscountedPrice()).equals(getTippedDiscountedPrice()),
                "AbsoluteTip (%s) + DiscountedPrice (%s) should give TippedDiscountedPrice (%s)",
                getAbsoluteTip(), getDiscountedPrice(), getTippedDiscountedPrice());

        Preconditions.checkArgument(
                getDiscountedPrice().plus(getRelativeTip()).equals(getTippedDiscountedPrice()),
                "RelativeTip (%s) + DiscountedPrice (%s) should give TippedDiscountedPrice (%s)",
                getDiscountedPrice(), getRelativeDiscount(), getTippedDiscountedPrice());
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
}
