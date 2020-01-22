package de.skuzzle.cmp.collaborativeorder.orderpage;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getOrderPage(
            @RequestParam(name = "status", defaultValue = "openForJoining") String orderStatus) {
        return new ModelAndView("order/orderPage.html", Map.of("status", orderStatus));
    }
}
