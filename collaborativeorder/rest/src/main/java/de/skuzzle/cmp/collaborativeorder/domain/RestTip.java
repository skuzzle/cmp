package de.skuzzle.cmp.collaborativeorder.domain;

import com.google.common.base.Preconditions;

public class RestTip {

    private final RestPercentage percentage;
    private final RestMoney absolute;

    private RestTip(RestPercentage percentage, RestMoney absolute) {
        Preconditions.checkArgument(percentage != null ^ absolute != null, "tip can either be relative OR absolute");
        this.percentage = percentage;
        this.absolute = absolute;
    }

    public static RestTip fromDomain(Tip tip) {
        if (tip.isAbsolute()) {
            return new RestTip(null, RestMoney.fromDomain(tip.getAbsolute()));
        }
        return new RestTip(RestPercentage.fromDomain(tip.getPercentage()), null);
    }

    public RestPercentage getPercentage() {
        return this.percentage;
    }

    public RestMoney getAbsolute() {
        return this.absolute;
    }

}
