package de.skuzzle.cmp.calculator;

import static de.skuzzle.cmp.calculator.Amount.times;
import static de.skuzzle.cmp.calculator.Money.money;
import static de.skuzzle.cmp.calculator.Percentage.percent;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CartTest {

    private final LineItem pizzaSalami = LineItem.withNameAndPrice("Pizza Salami", money(10));
    private final LineItem bread = LineItem.withNameAndPrice("Pizza Brötchen", money(5));
    private final LineItem pizzaTuna = LineItem.withNameAndPrice("Pizza Tuna", money(15));

    private Cart sampleCart() {
        return new Cart().transaction(transaction -> {
            final OrderingPerson simon = transaction.orderingPerson("Simon");
            simon.addLineItem(pizzaSalami);
            simon.addLineItem(bread.withAmount(Amount.times(2)));
            final OrderingPerson timo = transaction.orderingPerson("Timo");
            timo.addLineItem(pizzaTuna);
        });
    }

    @Test
    void testTotalsOnCartNoDiscountNoTip() throws Exception {
        final Cart cart = sampleCart();

        final CalculatedPrices totals = cart.getCalculatedPrices();
        assertPlausibleTotals(totals);
        assertThat(totals.getOriginalPrice()).isEqualTo(money(35.0));
    }

    @Test
    void testTotalsOnCartWithAbsoluteDiscount() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> transaction.withGlobalDiscount(Discount.absolute(money(5))));

        final CalculatedPrices totals = cart.getCalculatedPrices();
        assertPlausibleTotals(totals);
        assertThat(totals.getOriginalPrice()).isEqualTo(money(35));
    }

    @Test
    void testTotalsOnCartWithRelativeDiscount() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> transaction.withGlobalDiscount(Discount.relative(percent(0.1))));

        final CalculatedPrices totals = cart.getCalculatedPrices();
        assertPlausibleTotals(totals);
        assertThat(totals.getOriginalPrice()).isEqualTo(money(35));
    }

    @Test
    void testTotalsOnLineItemsWithAbsoluteDiscount() throws Exception {
        sampleCart()
                .transaction(transaction -> transaction.withGlobalDiscount(Discount.absolute(money(5))))
                .lineItems()
                .map(LineItem::getCalculatedPrices)
                .forEach(this::assertPlausibleTotals);
    }

    @Test
    void testTotalsOnLineItemsWithRelativeDiscount() throws Exception {
        sampleCart()
                .transaction(transaction -> transaction.withGlobalDiscount(Discount.relative(percent(0.1))))
                .lineItems()
                .map(LineItem::getCalculatedPrices)
                .forEach(this::assertPlausibleTotals);
    }

    @Test
    void testTotalsOnOrderingPersonWithTip() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> transaction.orderingPerson("Simon").payTip(Tip.absolute(money(1.0))));

        final OrderingPerson simon = cart.orderingPerson("Simon");

        assertPlausibleTotals(simon.getCalculatedPrices());
    }

    @Test
    void testTotalsOnCartWithTip() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> {
                    transaction.orderingPerson("Simon").payTip(Tip.absolute(money(1.0)));
                    transaction.orderingPerson("Timo").payTip(Tip.relative(percent(0.1)));
                });

        assertPlausibleTotals(cart.getCalculatedPrices());
    }

    @Test
    void testTotalsOnOrderingPersonWithTipAndDiscount() throws Exception {
        sampleCart()
                .transaction(transaction -> transaction
                        .withGlobalDiscount(Discount.absolute(money(5.0)))
                        .orderingPerson("Simon").payTip(Tip.relative(percent(0.1))))
                .lineItems()
                .map(LineItem::getCalculatedPrices)
                .forEach(this::assertPlausibleTotals);
    }

    @Test
    void testTotalsOnCartPersonWithTipAndDiscount() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> {
                    transaction.withGlobalDiscount(Discount.absolute(money(5.0)));
                    transaction.orderingPerson("Simon").payTip(Tip.relative(percent(0.1)));
                    transaction.orderingPerson("Timo").payTip(Tip.absolute(money(2.0)));
                });

        assertPlausibleTotals(cart.getCalculatedPrices());
    }

    private void assertPlausibleTotals(CalculatedPrices totals) {
        final Money originalPrice = totals.getOriginalPrice();
        final Money discount = totals.getAbsoluteDiscount();
        final Percentage relativeDiscount = totals.getRelativeDiscount();
        final Money discountedPrice = totals.getDiscountedPrice();
        final Money tippedDiscountedPrice = totals.getTippedDiscountedPrice();
        final Money tip = totals.getAbsoluteTip();
        final Percentage relativeTip = totals.getRelativeTip();

        assertThat(discount.plus(discountedPrice))
                .as("Discount + DiscountedPrice should add up to original price")
                .isEqualTo(originalPrice);
        assertThat(relativeDiscount.complementary().from(originalPrice))
                .as("Relative discount from original price should give the calculated discounted price")
                .isEqualTo(discountedPrice);

        assertThat(tip.plus(discountedPrice))
                .as("Tip + DiscountedPrice should give the tipped discounted price")
                .isEqualTo(tippedDiscountedPrice);
        assertThat(discountedPrice.plus(relativeTip))
                .as("DiscountPrice + Relative tip percentage should give the tipped discount price")
                .isEqualTo(tippedDiscountedPrice);
    }

    @Test
    void testFormatWithAbsoluteDiscount() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> {
                    transaction.withGlobalDiscount(Discount.absolute(money(5.0)));
                    transaction.orderingPerson("Simon").payTip(Tip.relative(percent(0.1)));
                    transaction.orderingPerson("Timo").payTip(Tip.absolute(money(2.0)));
                });
        System.out.println(cart.format());
    }

    @Test
    void testFormatWithRelativeDiscount() throws Exception {
        final Cart cart = sampleCart()
                .transaction(transaction -> {
                    transaction.withGlobalDiscount(Discount.relative(percent(0.1)));
                    transaction.orderingPerson("Simon").payTip(Tip.relative(percent(0.1)));
                    transaction.orderingPerson("Timo").payTip(Tip.absolute(money(2.0)));
                });
        System.out.println(cart.format());
    }

    @Test
    void testEcht() throws Exception {
        final Cart cart = Cart.newEmptyCart()
                .transaction(transaction -> {
                    transaction.orderingPerson("Chris")
                            .addLineItem(LineItem.withNameAndPrice("Pizza Brötchen", money(4.49)).withAmount(times(3)))
                            .addLineItem(LineItem.withNameAndPrice("Knoblauch Dip", money(0.99)).withAmount(times(2)));
                    transaction.orderingPerson("Simon")
                            .payTip(Tip.absolute(money(1)))
                            .addLineItem(LineItem.withNameAndPrice("Pizza Tuna 25cm", money(8.49)));
                    transaction.orderingPerson("Timo")
                            .addLineItem(LineItem.withNameAndPrice("Conchita 25cm", money(9.99)));
                    transaction.orderingPerson("Robert")
                            .addLineItem(LineItem.withNameAndPrice("Pizza Bombay 28cm", money(9.99)));
                })
                .transaction(transaction -> transaction.withDiscountedPrice(Money.money(35.44)));
        System.out.println(cart.format());
    }

    @Test
    void testEchtSuggestTip() throws Exception {
        final Cart cart = Cart.newEmptyCart()
                .transaction(transaction -> {
                    transaction.orderingPerson("Chris")
                            .addLineItem(LineItem.withNameAndPrice("Pizza Brötchen", money(4.49)).withAmount(times(3)))
                            .addLineItem(LineItem.withNameAndPrice("Knoblauch Dip", money(0.99)).withAmount(times(2)));
                    transaction.orderingPerson("Simon")
                            .payTip(Tip.absolute(money(1)))
                            .addLineItem(LineItem.withNameAndPrice("Pizza Tuna 25cm", money(8.49)));
                    transaction.orderingPerson("Timo")
                            .addLineItem(LineItem.withNameAndPrice("Conchita 25cm", money(9.99)));
                    transaction.orderingPerson("Robert")
                            .addLineItem(LineItem.withNameAndPrice("Pizza Bombay 28cm", money(9.99)));
                })
                .transaction(transaction -> transaction.withDiscountedPrice(Money.money(35.44)))
                .transaction(transaction -> transaction.suggestTip(new TipSuggestion(percent(0.1), money(1))));
        System.out.println(cart.format());
    }
}
