package de.skuzzle.cmp.collaborativeorder.domain;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class ParticipationService {

    private final CollaborativeOrderRepository collaborativeOrderRepository;

    public ParticipationService(CollaborativeOrderRepository collaborativeOrderRepository) {
        this.collaborativeOrderRepository = collaborativeOrderRepository;
    }

    // participant view

    private CollaborativeOrder getOrder(String collaborativeOrderId) {
        Preconditions.checkArgument(collaborativeOrderId != null, "collaborativeOrderId must not be null");
        return collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow(() -> new UnknownOrderException(collaborativeOrderId));
    }

    public Participant join(String collaborativeOrderId, UserId userId) {
        final CollaborativeOrder collaborativeOrder = getOrder(collaborativeOrderId);
        final Participant participant = collaborativeOrder.incoporateUser(userId);
        collaborativeOrderRepository.save(collaborativeOrder);
        return participant;
    }

    public void leave(String collaborativeOrderId, UserId userId) {
        final CollaborativeOrder collaborativeOrder = getOrder(collaborativeOrderId);
        collaborativeOrder.expel(userId);
        collaborativeOrderRepository.save(collaborativeOrder);
    }

    public Participant addLineItem(String collaborativeOrderId, UserId userId, LineItem lineItem) {
        final CollaborativeOrder collaborativeOrder = getOrder(collaborativeOrderId);
        final Participant participant = collaborativeOrder.addLineItem(userId, lineItem);
        collaborativeOrderRepository.save(collaborativeOrder);
        return participant;
    }

    public Participant payTip(String collaborativeOrderId, UserId userId, Tip tip) {
        final CollaborativeOrder collaborativeOrder = getOrder(collaborativeOrderId);
        final Participant participant = collaborativeOrder.withTipBy(userId, tip);
        collaborativeOrderRepository.save(collaborativeOrder);
        return participant;
    }

    public Participant setReadyToOrder(String collaborativeOrderId, UserId userId, boolean readyToOrder) {
        final CollaborativeOrder collaborativeOrder = getOrder(collaborativeOrderId);
        final Participant participant = collaborativeOrder.participantReady(userId, readyToOrder);
        collaborativeOrderRepository.save(collaborativeOrder);
        return participant;
    }

    public Participant getParticipation(String collaborativeOrderId, UserId userId) {
        return getOrder(collaborativeOrderId).participantWithId(userId);
    }
}
