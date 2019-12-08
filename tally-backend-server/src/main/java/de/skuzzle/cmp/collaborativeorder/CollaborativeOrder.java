package de.skuzzle.cmp.collaborativeorder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.collections.Lists;

@Document
public class CollaborativeOrder {

    @Id
    private String id;
    @Version
    private int version;

    private String name;

    private boolean openForJoining;
    private boolean openForModify;
    private boolean orderPlaced;

    private final UserId organisatorId;
    private final List<Participant> participants;

    private Discount discount;

    @Transient
    private CalculatedPrices calculatedPrices;

    private CollaborativeOrder(String name, boolean openForJoining, boolean openForModify, boolean orderPlaced,
            UserId organisatorId,
            List<Participant> participants, Discount discount) {
        this.name = name;
        this.openForJoining = openForJoining;
        this.openForModify = openForJoining;
        this.orderPlaced = orderPlaced;
        this.organisatorId = organisatorId;
        this.participants = participants;
        this.discount = discount;

        this.updateCalculation();
    }

    public static CollaborativeOrder create(UserId organisatorId, String name) {
        Preconditions.checkArgument(organisatorId != null, "organisatorId must not be null");
        Preconditions.checkArgument(name != null, "name must not be null");

        return new CollaborativeOrder(name, true, true, false, organisatorId, new ArrayList<>(),
                Discount.NONE);
    }

    public String getId() {
        return this.id;
    }

    public boolean isOrganizedBy(UserId user) {
        return this.organisatorId.equals(user);
    }

    public String getName() {
        return this.name;
    }

    CollaborativeOrder updateName(String newName) {
        Preconditions.checkArgument(newName != null, "newName must not be null");
        this.name = newName;
        return this;
    }

    public boolean isReadyToOrder() {
        return this.participants.stream().allMatch(Participant::isReadyToOrder);
    }

    public boolean isOrderPlaced() {
        return this.orderPlaced;
    }

    public void placeOrder() {
        Preconditions.checkState(this.isReadyToOrder(), "Not every participant has placed its order");
        this.closeForModify();
        this.orderPlaced = true;
    }

    public boolean isOpenForJoining() {
        return this.openForJoining;
    }

    void closeForJoining() {
        this.openForJoining = false;
    }

    public boolean isOpenForModify() {
        return this.openForModify;
    }

    void closeForModify() {
        this.closeForJoining();
        this.openForModify = false;
    }

    Participant incoporateUser(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        Preconditions.checkState(this.id != null,
                "Can not incoporate user %s: collaborative order has not been persisted yet", userId);
        Preconditions.checkState(this.openForJoining, "Can not incorporate user %s: order is closed for joining",
                userId);
        final boolean exists = participants.stream().anyMatch(p -> p.getUserId().equals(userId));
        Preconditions.checkArgument(!exists, "%s already participates in this order");

        final Participant newParticipation = Participant.newParticipation(this.id, userId);
        this.participants.add(newParticipation);

        this.updateCalculation();
        return newParticipation;
    }

    CollaborativeOrder expel(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        Preconditions.checkState(openForModify, "Can not expel user %s: order is closed for modification", userId);
        final boolean removed = this.participants.removeIf(p -> p.getUserId().equals(userId));
        Preconditions.checkArgument(removed, "Collaborative order does not have a participant with user id %s", userId);

        this.updateCalculation();
        return this;
    }

    CollaborativeOrder withGlobalDiscount(Discount discount) {
        Preconditions.checkArgument(discount != null, "discount must not be null");
        this.discount = discount;

        this.updateCalculation();
        return this;
    }

    public Discount getDiscount() {
        return this.discount;
    }

    CollaborativeOrder addLineItem(UserId userId, LineItem lineItem) {
        Preconditions.checkState(isOpenForModify(),
                "Can not add line item %s to user %s: Order is closed for modification", lineItem, userId);

        participantWithId(userId).addLineItem(lineItem);
        this.updateCalculation();

        return this;
    }

    CollaborativeOrder withTipBy(UserId userId, Tip tip) {
        Preconditions.checkState(isOpenForModify(),
                "Can not set tip %s for user %s: Order is closed for modification", tip, userId);

        participantWithId(userId).payTip(tip);
        this.updateCalculation();

        return this;
    }

    public CollaborativeOrder participantReady(UserId userId, boolean readyToOrder) {
        Preconditions.checkState(isOpenForModify(),
                "Can not set readyToOrder for user %s: Order is closed for modification", userId);
        participantWithId(userId).setReadyToOrder(readyToOrder);
        return this;
    }

    public Participant participantWithId(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");

        final int i = Lists.firstIndexOf(participants, p -> p.getUserId().equals(userId));
        Preconditions.checkArgument(i >= 0, "Collaborative order does not have a participant with user id %s", userId);
        return participants.get(i);
    }

    private Money totalSumOfItemPrices() {
        return this.participants.stream()
                .map(Participant::sumOfItemPrices)
                .reduce(Money.ZERO, Money::plus);
    }

    public CalculatedPrices getCalculatedPrices() {
        return this.calculatedPrices;
    }

    private CalculatedPrices updateCalculation() {
        final Money totalOriginalPrice = totalSumOfItemPrices();
        if (totalOriginalPrice.equals(Money.ZERO)) {
            return CalculatedPrices.ZERO;
        }

        final Money absoluteGlobalDiscount = this.discount.getAbsoluteValue(totalOriginalPrice);

        final Money discountedPrice = totalOriginalPrice.minus(absoluteGlobalDiscount);
        final Percentage relativeDiscount = discountedPrice.inRelationTo(totalOriginalPrice).complementary();

        final Money absoluteTip = participants.stream()
                .map(participant -> participant.updateCalculation(absoluteGlobalDiscount, totalOriginalPrice))
                .map(CalculatedPrices::getAbsoluteTip)
                .reduce(Money.ZERO, Money::plus);

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

}
