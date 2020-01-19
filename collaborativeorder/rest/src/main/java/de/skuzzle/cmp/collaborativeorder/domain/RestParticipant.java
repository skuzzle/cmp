package de.skuzzle.cmp.collaborativeorder.domain;

import java.util.List;
import java.util.stream.Collectors;

public class RestParticipant {

    private final String id;
    private final List<RestLineItem> lineItems;
    private final RestCalculatedPrices calculatedPrices;
    private final RestTip tip;
    private final boolean readyToOrder;

    private RestParticipant(String id, List<RestLineItem> lineItems, RestCalculatedPrices calculatedPrices, RestTip tip,
            boolean readyToOrder) {
        this.id = id;
        this.lineItems = lineItems;
        this.calculatedPrices = calculatedPrices;
        this.tip = tip;
        this.readyToOrder = readyToOrder;
    }

    public static RestParticipant fromDomain(Participant participation) {
        final List<RestLineItem> lineItems = participation.lineItems().stream()
                .map(RestLineItem::fromDomain)
                .collect(Collectors.toList());
        return new RestParticipant(
                participation.getId(),
                lineItems,
                RestCalculatedPrices.fromDomain(participation.getCalculatedPrices()),
                RestTip.fromDomain(participation.getTip()),
                participation.isReadyToOrder());
    }

    public String getId() {
        return this.id;
    }

    public List<RestLineItem> getLineItems() {
        return this.lineItems;
    }

    public RestCalculatedPrices getCalculatedPrices() {
        return this.calculatedPrices;
    }

    public RestTip getTip() {
        return this.tip;
    }

    public boolean isReadyToOrder() {
        return this.readyToOrder;
    }

}
