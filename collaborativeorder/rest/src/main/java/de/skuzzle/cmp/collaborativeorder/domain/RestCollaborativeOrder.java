package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.List;
import java.util.stream.Collectors;

public class RestCollaborativeOrder {

    private final String id;
    private final String name;
    private final boolean openForJoining;
    private final boolean openForModify;
    private final boolean orderPlaced;

    private final RestDiscount discount;

    private final List<String> participantIds;
    private final RestCalculatedPrices calculatedPrices;

    private RestCollaborativeOrder(String id, String name, boolean openForJoining, boolean openForModify,
            boolean orderPlaced, RestDiscount discount, List<String> participantIds,
            RestCalculatedPrices calculatedPrices) {
        this.id = id;
        this.name = name;
        this.openForJoining = openForJoining;
        this.openForModify = openForModify;
        this.orderPlaced = orderPlaced;
        this.discount = discount;
        this.participantIds = participantIds;
        this.calculatedPrices = calculatedPrices;
    }

    public static RestCollaborativeOrder fromDomain(CollaborativeOrder order) {
        final List<String> participantIds = order.participants().stream()
                .map(Participant::getId)
                .collect(Collectors.toList());

        return new RestCollaborativeOrder(order.getId(),
                order.getName(),
                order.isOpenForJoining(),
                order.isOpenForModify(),
                order.isOrderPlaced(),
                RestDiscount.fromDomain(order.getDiscount()),
                participantIds,
                RestCalculatedPrices.fromDomain(order.getCalculatedPrices()));
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isOpenForJoining() {
        return this.openForJoining;
    }

    public boolean isOpenForModify() {
        return this.openForModify;
    }

    public boolean isOrderPlaced() {
        return this.orderPlaced;
    }

    public RestDiscount getDiscount() {
        return this.discount;
    }

    public List<String> getParticipantIds() {
        return this.participantIds;
    }

    public RestCalculatedPrices getCalculatedPrices() {
        return this.calculatedPrices;
    }
}
