package de.skuzzle.cmp.collaborativeorder;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class ParticipationService {

    private final CollaborativeOrderRepository collaborativeOrderRepository;

    public ParticipationService(CollaborativeOrderRepository collaborativeOrderRepository) {
        this.collaborativeOrderRepository = collaborativeOrderRepository;
    }

    // participant view

    public Participant join(String collaborativeOrderId, UserId userId) {
        Preconditions.checkArgument(collaborativeOrderId != null, "collaborativeOrderId must not be null");
        final CollaborativeOrder collaborativeOrder = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow();

        final Participant participant = collaborativeOrder.incoporateUser(userId);
        collaborativeOrderRepository.save(collaborativeOrder);
        return participant;
    }

    public void leave(String collaborativeOrderId, UserId userId) {
        final CollaborativeOrder collaborativeOrder = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow();
        collaborativeOrder.expel(userId);
        collaborativeOrderRepository.save(collaborativeOrder);
    }

    public Participant addLineItem(String collaborativeOrderId, UserId userId, LineItem lineItem) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        final Participant participant = order.addLineItem(userId, lineItem);
        collaborativeOrderRepository.save(order);
        return participant;
    }

    public Participant payTip(String collaborativeOrderId, UserId userId, Tip tip) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        final Participant participant = order.withTipBy(userId, tip);
        collaborativeOrderRepository.save(order);
        return participant;
    }

    public Participant setReadyToOrder(String collaborativeOrderId, UserId userId, boolean readyToOrder) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId).orElseThrow();
        final Participant participant = order.participantReady(userId, readyToOrder);
        collaborativeOrderRepository.save(order);
        return participant;
    }

    public Participant getParticipation(String collaborativeOrderId, UserId userId) {
        final CollaborativeOrder collaborativeOrder = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow();
        return collaborativeOrder.participantWithId(userId);
    }
}
