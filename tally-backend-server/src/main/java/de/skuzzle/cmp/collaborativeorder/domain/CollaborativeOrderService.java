package de.skuzzle.cmp.collaborativeorder.domain;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class CollaborativeOrderService {

    private final CollaborativeOrderRepository collaborativeOrderRepository;

    public CollaborativeOrderService(CollaborativeOrderRepository collaborativeOrderRepository) {
        this.collaborativeOrderRepository = collaborativeOrderRepository;
    }

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

    public CollaborativeOrder placeOrder(String collaborativeOrderId, UserId organisatorId) {
        final CollaborativeOrder order = getOrderForOrganizator(collaborativeOrderId, organisatorId);
        order.placeOrder();
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder setDiscount(String collaborativeOrderId, UserId organizatorId, Discount discount) {
        final CollaborativeOrder order = getOrderForOrganizator(collaborativeOrderId, organizatorId);
        order.withGlobalDiscount(discount);
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder setDiscountedPrice(String collaborativeOrderId, UserId organizatorId,
            Money discountedPrice) {
        final CollaborativeOrder order = getOrderForOrganizator(collaborativeOrderId, organizatorId);
        order.withGlobalDiscountedPrice(discountedPrice);
        return collaborativeOrderRepository.save(order);
    }

}
