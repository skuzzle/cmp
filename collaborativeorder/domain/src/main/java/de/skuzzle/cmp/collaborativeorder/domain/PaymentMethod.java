package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.Objects;

import com.google.common.base.Preconditions;

public final class PaymentMethod {

    private final String name;
    private final String details;

    private PaymentMethod(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public static PaymentMethod withName(String name) {
        return withNameAndDetails(name, "");
    }

    public static PaymentMethod withNameAndDetails(String name, String details) {
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(details != null, "details msut not be null");
        return new PaymentMethod(name, details);
    }

    public String getName() {
        return this.name;
    }

    public String getDetails() {
        return this.details;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof PaymentMethod
                && Objects.equals(name, ((PaymentMethod) obj).name);

    }

}
