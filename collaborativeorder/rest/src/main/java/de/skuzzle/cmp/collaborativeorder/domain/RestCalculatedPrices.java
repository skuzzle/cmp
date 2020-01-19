package de.skuzzle.cmp.collaborativeorder.domain;

public class RestCalculatedPrices {

    private final RestMoney originalPrice;
    private final RestMoney discountedPrice;
    private final RestMoney absoluteDiscount;
    private final RestPercentage relativeDiscount;
    private final RestMoney tippedDiscountedPrice;
    private final RestMoney absoluteTip;
    private final RestPercentage relativeTip;

    private RestCalculatedPrices(
            RestMoney originalPrice,
            RestMoney discountedPrice,
            RestMoney absoluteDiscount,
            RestPercentage relativeDiscount,
            RestMoney tippedDiscountedPrice,
            RestMoney absoluteTip,
            RestPercentage relativeTip) {
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.absoluteDiscount = absoluteDiscount;
        this.relativeDiscount = relativeDiscount;
        this.tippedDiscountedPrice = tippedDiscountedPrice;
        this.absoluteTip = absoluteTip;
        this.relativeTip = relativeTip;
    }

    public static RestCalculatedPrices fromDomain(CalculatedPrices calculatedPrices) {
        return new RestCalculatedPrices(
                RestMoney.fromDomain(calculatedPrices.getOriginalPrice()),
                RestMoney.fromDomain(calculatedPrices.getDiscountedPrice()),
                RestMoney.fromDomain(calculatedPrices.getAbsoluteDiscount()),
                RestPercentage.fromDomain(calculatedPrices.getRelativeDiscount()),
                RestMoney.fromDomain(calculatedPrices.getTippedDiscountedPrice()),
                RestMoney.fromDomain(calculatedPrices.getAbsoluteTip()),
                RestPercentage.fromDomain(calculatedPrices.getRelativeTip()));
    }

    public RestMoney getOriginalPrice() {
        return this.originalPrice;
    }

    public RestMoney getDiscountedPrice() {
        return this.discountedPrice;
    }

    public RestMoney getAbsoluteDiscount() {
        return this.absoluteDiscount;
    }

    public RestPercentage getRelativeDiscount() {
        return this.relativeDiscount;
    }

    public RestMoney getTippedDiscountedPrice() {
        return this.tippedDiscountedPrice;
    }

    public RestMoney getAbsoluteTip() {
        return this.absoluteTip;
    }

    public RestPercentage getRelativeTip() {
        return this.relativeTip;
    }

}
