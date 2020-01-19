package de.skuzzle.cmp.collaborativeorder.domain;

import java.math.BigDecimal;

import com.google.common.base.Preconditions;

public class RestMoney {

    private final BigDecimal value;

    private RestMoney(BigDecimal value) {
        Preconditions.checkArgument(value != null, "value must not be null");
        this.value = value;
    }

    public static RestMoney fromDomain(Money money) {
        return new RestMoney(money.getMonetaryAmount());
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Money toDomain() {
        return Money.money(value);
    }

}
