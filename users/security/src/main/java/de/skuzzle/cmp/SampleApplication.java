package de.skuzzle.cmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.skuzzle.cmp.users.registration.ConfirmationToken;
import de.skuzzle.cmp.users.registration.RegisterFailedException;
import de.skuzzle.cmp.users.registration.RegisterUserService;

@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Autowired
    void registerMe(RegisterUserService registerUserService) {
        try {
            final ConfirmationToken confirmationToken = registerUserService.registerUser("Simon", "cmp@taddiken.online",
                    "qwertz");
            registerUserService.confirmRegistration(confirmationToken.token());
        } catch (final RegisterFailedException e) {
            // already exists
        }
    }

}
