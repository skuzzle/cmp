package de.skuzzle.cmp.collaborativeorder.domain;

public class RestAmount {

    private final int value;

    private RestAmount(int amount) {
        this.value = amount;
    }

    public static RestAmount fromDomain(Amount amount) {
        return new RestAmount(amount.getAmount());
    }

    public int getValue() {
        return this.value;
    }

}
