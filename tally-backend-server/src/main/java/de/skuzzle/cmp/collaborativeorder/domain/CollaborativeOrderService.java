package de.skuzzle.cmp.collaborativeorder.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CollaborativeOrderService {

    private static final Logger log = LoggerFactory.getLogger(CollaborativeOrderService.class);

    private final CollaborativeOrderRepository collaborativeOrderRepository;

    public CollaborativeOrderService(CollaborativeOrderRepository collaborativeOrderRepository) {
        this.collaborativeOrderRepository = collaborativeOrderRepository;
    }

    public CollaborativeOrder organizeCollaborativeOrder(String name, UserId organisatorId) {
        final CollaborativeOrder order = CollaborativeOrder.create(organisatorId, name);
        return collaborativeOrderRepository.save(order);
    }

    public CollaborativeOrder getOrderForOrganizator(String collaborativeOrderId, UserId organisatorId) {
        final CollaborativeOrder order = collaborativeOrderRepository.findById(collaborativeOrderId)
                .orElseThrow(() -> new UnknownOrderException(collaborativeOrderId));

        if (!order.isOrganizedBy(organisatorId)) {
            log.warn("User {} tried to access order {} but is not the organisator", organisatorId,
                    collaborativeOrderId);
            throw new UnknownOrderException(collaborativeOrderId);
        }
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
