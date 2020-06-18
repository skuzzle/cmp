package de.skuzzle.cmp.users.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegisterUserService registerUserService;

    public RegistrationController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration/registrationPage";
    }

    @PostMapping("/registration")
    public String performRegistration(String name, String email, String password, String passwordRepeat) {
        if (!passwordRepeat.equals(password)) {
            throw new RegisterFailedException("Password mismatch");
        }
        registerUserService.registerUser(name, email, password);
        return "redirect:/registration?success";
    }

    @ExceptionHandler(RegisterFailedException.class)
    public String handleRegisterFailedException(RegisterFailedException e) {
        return "redirect:/registration?failure";
    }
}
