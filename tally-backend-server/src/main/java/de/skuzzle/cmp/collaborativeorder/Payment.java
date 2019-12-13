package de.skuzzle.cmp.collaborativeorder;

public class Payment {

    private final String id;
    private final Money amountPaid;
    private final PaymentMethod method;
    private boolean approved;

    public Money getAmountPaid() {
        return this.amountPaid;
    }
}
