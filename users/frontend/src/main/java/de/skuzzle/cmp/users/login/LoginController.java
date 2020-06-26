package de.skuzzle.cmp.users.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    public LoginController() {
        System.out.println("sdasd");
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }
}
