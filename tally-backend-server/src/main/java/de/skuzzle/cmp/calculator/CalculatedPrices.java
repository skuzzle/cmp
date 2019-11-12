package de.skuzzle.cmp.calculator;

final class CalculatedPrices {

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
