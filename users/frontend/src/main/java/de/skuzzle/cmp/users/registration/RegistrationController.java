package de.skuzzle.cmp.users.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    private final RegisterUserService registerUserService;

    public RegistrationController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration/registration";
    }

    @GetMapping("/registration/success")
    public String registrationSuccessPage() {
        return "registration/registrationSuccess";
    }

    @GetMapping("/registration/confirm/{confirmationToken}")
    public String confirmRegistration(@PathVariable String confirmationToken) {
        final RegisteredUser confirmRegistration = registerUserService.confirmRegistration(confirmationToken);
        throw new UnsupportedOperationException("??");

    }

    @PostMapping("/registration")
    public ModelAndView performRegistration(String name, String email, String password, String passwordRepeat) {

        final ModelAndView result = new ModelAndView("registration/registration");
        result.addObject("name", name);
        result.addObject("email", email);
        result.addObject("password", password);

        if (!passwordRepeat.equals(password)) {
            result.addObject("failure", true);
            result.addObject("failureMessage", "Registration failed: Passwords do not match");
            return result;
        }

        try {
            registerUserService.registerUser(name, email, password);
            return new ModelAndView("redirect:/registration/success");
        } catch (final RegisterFailedException e) {
            result.addObject("failure", true);
            result.addObject("failureMessage", "Registration failed: E-Mail address already taken");

            return result;
        }
    }
}
