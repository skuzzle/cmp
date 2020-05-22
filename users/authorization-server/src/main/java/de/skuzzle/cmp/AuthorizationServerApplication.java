package de.skuzzle.cmp;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.skuzzle.cmp.common.http.RequestIdFilter;
import de.skuzzle.cmp.common.http.RequestLoggingFilter;
import de.skuzzle.cmp.common.http.ResponseSizeTrackingFilter;
import de.skuzzle.cmp.users.registration.ConfirmationToken;
import de.skuzzle.cmp.users.registration.RegisterFailedException;
import de.skuzzle.cmp.users.registration.RegisterUserService;

@SpringBootApplication
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

    @Bean
    public Filter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    public Filter trackResponseSizes() {
        return new ResponseSizeTrackingFilter();
    }

    @Bean
    public Filter requestLoggingFilter() {
        return new RequestLoggingFilter();
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
