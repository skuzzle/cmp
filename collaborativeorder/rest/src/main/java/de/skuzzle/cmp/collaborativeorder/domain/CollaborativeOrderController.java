package de.skuzzle.cmp.collaborativeorder.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.skuzzle.cmp.rest.auth.TallyUser;

@RestController
@RequestMapping(path = "/order")
public class CollaborativeOrderController {

    private final TallyUser currentUser;
    private final CollaborativeOrderService collaborativeOrderService;

    public CollaborativeOrderController(TallyUser currentUser, CollaborativeOrderService collaborativeOrderService) {
        this.currentUser = currentUser;
        this.collaborativeOrderService = collaborativeOrderService;
    }

    private UserId currentUser() {
        return UserId.of(currentUser.getSource(), currentUser.getId(), currentUser.isAnonymous());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public RestCollaborativeOrder organizeCollaborativeOrder(@PathVariable("name") String name) {
        final UserId organisator = currentUser();
        final CollaborativeOrder collaborativeOrder = collaborativeOrderService.organizeCollaborativeOrder(name,
                organisator);

        return RestCollaborativeOrder.fromDomain(collaborativeOrder);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestCollaborativeOrder getCollaborativeOrder(@PathVariable("id") String id) {
        final UserId organisator = currentUser();
        final CollaborativeOrder collaborativeOrder = collaborativeOrderService.getOrderForOrganizator(id, organisator);
        return RestCollaborativeOrder.fromDomain(collaborativeOrder);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/closeForJoin")
    @ResponseStatus(HttpStatus.OK)
    public void closeOrderForJoining(@PathVariable("id") String id) {
        final UserId organisator = currentUser();
        collaborativeOrderService.closeOrderForJoining(id, organisator);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/closeForModify")
    @ResponseStatus(HttpStatus.OK)
    public void closeOrderForModification(@PathVariable("id") String id) {
        final UserId organisator = currentUser();
        collaborativeOrderService.closeOrderForModification(id, organisator);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/placeOrder")
    @ResponseStatus(HttpStatus.OK)
    public void placeOrder(@PathVariable("id") String id) {
        final UserId organisator = currentUser();
        collaborativeOrderService.placeOrder(id, organisator);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/setDiscount")
    @ResponseStatus(HttpStatus.OK)
    public void setDiscount(@PathVariable("id") String id, @RequestBody RestDiscount discountRequest) {
        final UserId organisator = currentUser();
        final Discount discount = discountRequest.toDomain();
        collaborativeOrderService.setDiscount(id, organisator, discount);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/setDiscountedPrice")
    @ResponseStatus(HttpStatus.OK)
    public void setDiscountedPrice(@PathVariable("id") String id, @RequestBody RestMoney discountedPriceRequest) {
        final UserId organisator = currentUser();
        final Money discountedPrice = discountedPriceRequest.toDomain();
        collaborativeOrderService.setDiscountedPrice(id, organisator, discountedPrice);
    }
}
