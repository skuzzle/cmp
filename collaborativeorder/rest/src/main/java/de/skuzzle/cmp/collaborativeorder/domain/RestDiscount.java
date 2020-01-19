package de.skuzzle.cmp.collaborativeorder.domain;

import com.google.common.base.Preconditions;

public class RestDiscount {

    private final RestPercentage percentage;
    private final RestMoney absolute;

    private RestDiscount(RestPercentage percentage, RestMoney absolute) {
        Preconditions.checkArgument(percentage != null ^ absolute != null,
                "discount can either be relative OR absolute");
        this.percentage = percentage;
        this.absolute = absolute;
    }

    public static RestDiscount fromDomain(Discount discount) {
        if (discount.isAbsolute()) {
            return new RestDiscount(null, RestMoney.fromDomain(discount.getAbsolute()));
        }
        return new RestDiscount(RestPercentage.fromDomain(discount.getPercentage()), null);
    }

    public Discount toDomain() {
        if (percentage != null) {
            return Discount.relative(percentage.toDomain());
        }
        return Discount.absolute(absolute.toDomain());
    }

    public RestMoney getAbsolute() {
        return this.absolute;
    }

    public RestPercentage getPercentage() {
        return this.percentage;
    }
}
