package de.skuzzle.cmp.calculator;

import com.google.common.base.Preconditions;

class CartTransaction {

    private static final ThreadLocal<Cart> transactions = new ThreadLocal<>();

    public static void start(Cart cart) {
        Preconditions.checkState(cart != null, "cart for transaction must not be null");
        Preconditions.checkState(transactions.get() == null || transactions.get() == cart,
                "transaction already active for different cart");

        transactions.set(cart);
    }

    public static void assertActiveTransaction() {
        Preconditions.checkState(transactions.get() != null, "Can not modify cart: There is no active transaction");
    }

    public static void finishTransaction() {
        transactions.set(null);
    }
}
