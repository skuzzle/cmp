package de.skuzzle.cmp.collaborativeorder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Transient;

import com.google.common.base.Preconditions;

public class Participant {

    private final String id;
    private final String collaborativeOrderId;
    private final UserId userId;
    private final List<LineItem> lineItems;
    private Tip tip;
    private boolean readyToOrder;

    @Transient
    private CalculatedPrices calculatedPrices;

    private Participant(String id, String collaborativeOrderId, UserId userId, List<LineItem> lineItems, Tip tip) {
        this.id = id;
        this.collaborativeOrderId = collaborativeOrderId;
        this.userId = userId;
        this.lineItems = lineItems;
        this.tip = tip;

        this.calculatedPrices = CalculatedPrices.ZERO;
    }

    public static Participant newParticipation(String collaborativeOrderId, UserId userId) {
        Preconditions.checkArgument(collaborativeOrderId != null, "collaborativeOrderId must not be null");
        Preconditions.checkArgument(userId != null, "userId must not be null");

        final String id = UUID.randomUUID().toString();
        return new Participant(id, collaborativeOrderId, userId, new ArrayList<>(), Tip.NONE);
    }

    public String getId() {
        return this.id;
    }

    public UserId getUserId() {
        return this.userId;
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

    void setReadyToOrder(boolean readyToOrder) {
        this.readyToOrder = readyToOrder;
    }

    Money sumOfItemPrices() {
        return this.lineItems.stream()
                .map(LineItem::getOriginalPrice)
                .reduce(Money.ZERO, Money::plus);
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

    void setCalculatedPrices(CalculatedPrices calculatedPrices) {
        Preconditions.checkArgument(calculatedPrices != null);
        this.calculatedPrices = calculatedPrices;
    }
}
