package de.skuzzle.cmp.collaborativeorder.domain;

import java.math.BigDecimal;

import com.google.common.base.Preconditions;

public class RestPercentage {

    private final BigDecimal value;

    private RestPercentage(BigDecimal value) {
        Preconditions.checkArgument(value != null, "value must not be null");
        this.value = value;
    }

    public static RestPercentage fromDomain(Percentage percentage) {
        return new RestPercentage(percentage.getPercentage());
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Percentage toDomain() {
        return Percentage.percent(value);
    }

}
