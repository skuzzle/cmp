package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.UUID;

import com.google.common.base.Preconditions;

public class Payment {

    private final String id;
    private final Money amountPaid;
    private final PaymentMethod method;

    private Payment(String id, Money amountPaid, PaymentMethod method) {
        this.id = id;
        this.amountPaid = amountPaid;
        this.method = method;
    }

    public static Payment newOpenPayment(Money amountPaid, PaymentMethod method) {
        Preconditions.checkArgument(amountPaid != null, "amountPaid must not be null");
        Preconditions.checkArgument(method != null, "method must not be null");
        return new Payment(UUID.randomUUID().toString(), amountPaid, method);
    }

    public String getId() {
        return this.id;
    }

    public Money getAmountPaid() {
        return this.amountPaid;
    }

    public PaymentMethod getMethod() {
        return this.method;
    }

}
