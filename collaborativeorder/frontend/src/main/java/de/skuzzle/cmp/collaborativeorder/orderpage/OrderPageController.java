package de.skuzzle.cmp.collaborativeorder.orderpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.skuzzle.cmp.auth.TallyUser;

@Controller
@RequestMapping("/order")
public class OrderPageController {

    private final TallyUser currentUser;

    public OrderPageController(TallyUser currentUser) {
        this.currentUser = currentUser;
    }

    @ModelAttribute("user")
    public TallyUser getUser() {
        return currentUser;
    }

    @GetMapping
    public String getOrderPage() {
        return "order/orderPage.html";
    }
}
