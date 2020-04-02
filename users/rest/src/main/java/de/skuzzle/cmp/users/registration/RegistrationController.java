package de.skuzzle.cmp.users.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.skuzzle.cmp.common.ratelimit.ApiRateLimiter;
import de.skuzzle.cmp.common.ratelimit.RateLimitedOperations;

@RestController
public class RegistrationController {

    private final ApiRateLimiter<String> rateLimiter;
    private final RegisterUserService registerUserService;

    public RegistrationController(ApiRateLimiter<String> ratelimiter,
            RegisterUserService registerUserService) {
        this.rateLimiter = ratelimiter;
        this.registerUserService = registerUserService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegisterUserRequest registrationRequest) {
        rateLimiter.blockIfRateLimitIsExceeded(RateLimitedOperations.VERY_EXPENSIVE, registrationRequest.getEmail());

        registerUserService.registerUser(
                registrationRequest.getName(),
                registrationRequest.getEmail(),
                registrationRequest.getRawPassword());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration/confirm/{confirmationToken}")
    @ResponseStatus(HttpStatus.OK)
    public void confirmRegistration(@PathVariable String confirmationToken) {
        registerUserService.confirmRegistration(confirmationToken);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration/resetPassword")
    @ResponseStatus(HttpStatus.CREATED)
    public void requestResetPassword(@RequestBody ResetPasswordRequest resetPwRequest) {
        rateLimiter.blockIfRateLimitIsExceeded(RateLimitedOperations.VERY_EXPENSIVE, resetPwRequest.getEmail());
        registerUserService.requestResetPassword(resetPwRequest.getEmail());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration/resetPassword/confirm/{confirmationToken}")
    @ResponseStatus(HttpStatus.OK)
    public void confirmResetPassword(@PathVariable String confirmationToken,
            @RequestBody NewPasswordRequest newPasswordRequest) {
        registerUserService.confirmResetPassword(confirmationToken, newPasswordRequest.getNewRawPassword());
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorMessage> handleConfirmationFailedException(ConfirmationFailedException e) {
        final RestErrorMessage body = RestErrorMessage.of(e.getMessage(), e.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorMessage> handleRegistrationException(RegisterFailedException e) {
        final RestErrorMessage body = RestErrorMessage.of(e.getMessage(), e.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(body);
    }
}
