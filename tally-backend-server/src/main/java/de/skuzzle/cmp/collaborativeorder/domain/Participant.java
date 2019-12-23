package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Transient;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.common.table.ConsoleTable;
import de.skuzzle.cmp.common.table.RowData;

public class Participant {

    private final String id;
    private final UserId userId;
    private final List<LineItem> lineItems;
    private final List<Payment> payments;
    private Tip tip;
    private boolean readyToOrder;

    @Transient
    private CalculatedPrices calculatedPrices;

    private Participant(String id, UserId userId, List<LineItem> lineItems, List<Payment> payments, Tip tip) {
        this.id = id;
        this.userId = userId;
        this.lineItems = lineItems;
        this.payments = payments;
        this.tip = tip;

        this.calculatedPrices = CalculatedPrices.ZERO;
    }

    static Participant newParticipation(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");

        final String id = UUID.randomUUID().toString();
        return new Participant(id, userId, new ArrayList<>(), new ArrayList<>(), Tip.NONE);
    }

    public String getId() {
        return this.id;
    }

    public UserId getUserId() {
        return this.userId;
    }

    public boolean hasUserId(UserId other) {
        return this.getUserId().equals(other);
    }

    CollaborativeOrder leave(CollaborativeOrder order) {
        Preconditions.checkArgument(order != null, "order must not be null");
        return order.expel(userId);
    }

    Participant payTip(Tip tip) {
        Preconditions.checkArgument(tip != null, "tip must not be null");
        this.tip = tip;
        return this;
    }

    public boolean isReadyToOrder() {
        return this.readyToOrder;
    }

    Participant setReadyToOrder(boolean readyToOrder) {
        this.readyToOrder = readyToOrder;
        return this;
    }

    Participant registerPayment(Payment payment) {
        Preconditions.checkArgument(payment != null, "payment must not be null");
        this.payments.add(payment);
        return this;
    }

    public Money getPaidAmount() {
        return Money.sumBy(payments, Payment::getAmountPaid);
    }

    public Money getAmountToPay() {
        return calculatedPrices.getTippedDiscountedPrice();
    }

    Money sumOfItemPrices() {
        return Money.sumBy(lineItems, LineItem::getOriginalPrice);
    }

    CalculatedPrices updateCalculation(Money absoluteGlobalDiscount, Money totalSumOfItemPrices) {
        final Money originalPrice = sumOfItemPrices();
        if (originalPrice.equals(Money.ZERO)) {
            return CalculatedPrices.ZERO;
        }

        final Money discountedPrice = lineItems.stream()
                .map(li -> li.updateCalculation(absoluteGlobalDiscount, totalSumOfItemPrices))
                .map(CalculatedPrices::getDiscountedPrice)
                .reduce(Money.ZERO, Money::plus);

        final Money absoluteTip = tip.getAbsoluteValue(discountedPrice);
        final Percentage relativeDiscount = discountedPrice.inRelationTo(originalPrice).complementary();
        final Money absoluteDiscount = originalPrice.minus(discountedPrice);
        final Percentage relativeTip = absoluteTip.inRelationTo(discountedPrice);
        final Money tippedDiscountedPrice = discountedPrice.plus(absoluteTip);

        this.calculatedPrices = new CalculatedPrices(
                originalPrice,
                discountedPrice,
                absoluteDiscount,
                relativeDiscount,
                tippedDiscountedPrice,
                absoluteTip, relativeTip);
        return calculatedPrices;
    }

    Participant addLineItem(LineItem lineItem) {
        Preconditions.checkArgument(lineItem != null, "lineItem must not be null");
        this.lineItems.add(lineItem);
        return this;
    }

    public void toTable(ConsoleTable table) {
        table.addRow(RowData.of(
                "  " + getUserId(), "", "", 
                calculatedPrices.getOriginalPrice(),
                calculatedPrices.getRelativeDiscount(),
                calculatedPrices.getAbsoluteDiscount(),
                calculatedPrices.getDiscountedPrice(), 
                calculatedPrices.getRelativeTip(),
                calculatedPrices.getAbsoluteTip(),
                calculatedPrices.getTippedDiscountedPrice()));
        this.lineItems.forEach(item -> item.toTable(table));
    }
}
