package de.skuzzle.cmp.collaborativeorder;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class CollaborativeOrderService {

    private final CollaborativeOrderRepository collaborativeOrderRepository;

    public CollaborativeOrderService(CollaborativeOrderRepository collaborativeOrderRepository) {
        this.collaborativeOrderRepository = collaborativeOrderRepository;
    }

    // organizator view

    public CollaborativeOrder organizeCollaborativeOrder(String name, UserId organisatorId) {
        final CollaborativeOrder order = CollaborativeOrder.create(organisatorId, name);
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder getOrderForOrganizator(String collaborativeOrderId, UserId organisatorId) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        Preconditions.checkArgument(order.isOrganizedBy(organisatorId), "%s is not the organisator of order with id %s",
                collaborativeOrderId);
        return order;
    }

    public CollaborativeOrder closeOrderForJoining(String collaborativeOrderId, UserId organisatorId) {
        final CollaborativeOrder order = getOrderForOrganizator(collaborativeOrderId, organisatorId);
        order.closeForJoining();
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder closeOrderForModification(String collaborativeOrderId, UserId organisatorId) {
        final CollaborativeOrder order = getOrderForOrganizator(collaborativeOrderId, organisatorId);
        order.closeForModify();
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder setDiscount(String collaborativeOrderId, UserId organizatorId, Discount discount) {
        final CollaborativeOrder order = getOrderForOrganizator(collaborativeOrderId, organizatorId);
        order.withGlobalDiscount(discount);
        return collaborativeOrderRepository.save(order);
    }

    // participant view

    public Participant join(String collaborativeOrderId, UserId userId) {
        Preconditions.checkArgument(collaborativeOrderId != null, "collaborativeOrderId must not be null");
        final CollaborativeOrder collaborativeOrder = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow();

        final Participant participation = collaborativeOrder.incoporateUser(userId);
        collaborativeOrderRepository.save(collaborativeOrder);
        return participation;
    }

    public CollaborativeOrder leave(String collaborativeOrderId, UserId userId) {
        final CollaborativeOrder collaborativeOrder = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow();
        collaborativeOrder.expel(userId);
        return collaborativeOrderRepository.save(collaborativeOrder);
    }

    public CollaborativeOrder addLineItem(String collaborativeOrderId, UserId userId, LineItem lineItem) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        order.addLineItem(userId, lineItem);
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder setTip(String collaborativeOrderId, UserId userId, Tip tip) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        order.withTipBy(userId, tip);
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder setReadyToOrder(String collaborativeOrderId, UserId userId, boolean readyToOrder) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        order.participantReady(userId, readyToOrder);
        return collaborativeOrderRepository.save(order);
    }

    public Participant getParticipation(String collaborativeOrderId, UserId userId) {
        final CollaborativeOrder collaborativeOrder = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow();
        return collaborativeOrder.participantWithId(userId);
    }
}
