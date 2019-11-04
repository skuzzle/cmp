package de.skuzzle.cmp.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import io.micrometer.core.instrument.Metrics;

public class Cart {

    private final Map<String, OrderingPerson> orderingPersons = new HashMap<>();
    private Discount globalDiscount = Discount.none();
    private CalculatedPrices calculatedPrices;

    public static Cart newEmptyCart() {
        return new Cart();
    }

    public Cart transaction(Consumer<Cart> transaction) {
        final long transactionStart = System.currentTimeMillis();
        try {
            CartTransaction.start(this);
            transaction.accept(this);
            updateCalculation();
        } finally {
            CartTransaction.finishTransaction();
            final long transactionDuration = System.currentTimeMillis() - transactionStart;
            Metrics.timer("cart_transaction").record(transactionDuration, TimeUnit.MILLISECONDS);
        }
        return this;
    }

    public CalculatedPrices getCalculatedPrices() {
        Preconditions.checkState(calculatedPrices != null, "prices have not been calculated yet on this cart");
        return this.calculatedPrices;
    }

    private CalculatedPrices updateCalculation() {
        final Money totalOriginalPrice = calculateOriginalPrice();
        final Money absoluteGlobalDiscount = globalDiscount.getAbsoluteValue(totalOriginalPrice);

        final Money discountedPrice = totalOriginalPrice.minus(absoluteGlobalDiscount);
        final Percentage relativeDiscount = discountedPrice.inRelationTo(totalOriginalPrice).complementary();

        Money absoluteTip = Money.ZERO;
        for (final var orderingPerson : orderingPersons.values()) {
            final var calculatedPrices = orderingPerson.updateCalculation(absoluteGlobalDiscount,
                    totalOriginalPrice);

            absoluteTip = absoluteTip.plus(calculatedPrices.getAbsoluteTip());
        }

        final Percentage relativeTip = absoluteTip.inRelationTo(discountedPrice);
        final Money tippedDiscountedPrice = discountedPrice.plus(absoluteTip);

        this.calculatedPrices = new CalculatedPrices(totalOriginalPrice,
                discountedPrice,
                absoluteGlobalDiscount,
                relativeDiscount,
                tippedDiscountedPrice,
                absoluteTip,
                relativeTip);
        return calculatedPrices;
    }

    public Stream<LineItem> lineItems() {
        return orderingPersons.values().stream().map(OrderingPerson::getLineItems).flatMap(List::stream);
    }

    public OrderingPerson orderingPerson(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        return this.orderingPersons.computeIfAbsent(name, OrderingPerson::new);
    }

    private Money calculateOriginalPrice() {
        return orderingPersons.values().stream()
                .map(OrderingPerson::calculateOriginalPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    public Cart withGlobalDiscount(Discount globalDiscount) {
        Preconditions.checkArgument(globalDiscount != null, "globalDiscount must not be null");
        CartTransaction.assertActiveTransaction();

        this.globalDiscount = globalDiscount;
        return this;
    }

    public String format() {
        final StringBuilder stringBuilder = new StringBuilder()
                .append(this.calculatedPrices.format("Totals\t\t\t\t"))
                .append("\n");
        for (final OrderingPerson orderingPerson : orderingPersons.values()) {
            stringBuilder.append(orderingPerson.format("\t")).append("\n");
        }
        return stringBuilder.toString();
    }
}
