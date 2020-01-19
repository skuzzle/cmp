package de.skuzzle.cmp.collaborativeorder.orderpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderPageController {

    public OrderPageController() {
        // TODO Auto-generated constructor stub
        System.out.println();
    }

    @GetMapping
    public String getOrderPage() {
        return "order/orderPage.html";
    }
}
