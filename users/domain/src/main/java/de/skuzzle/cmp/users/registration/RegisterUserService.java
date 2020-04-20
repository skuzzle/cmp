package de.skuzzle.cmp.users.registration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.common.time.UTCDateTimeProvider;

public class RegisterUserService {

    private final PasswordEncoder passwordEncoder;
    private final UTCDateTimeProvider dateTimeProvider;
    private final RegisteredUserRepository registeredUserRepository;
    private final ApplicationEventPublisher eventPublisher;

    RegisterUserService(PasswordEncoder passwordEncoder, UTCDateTimeProvider dateTimeProvider,
            RegisteredUserRepository registeredUserRepository,
            ApplicationEventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.dateTimeProvider = dateTimeProvider;
        this.registeredUserRepository = registeredUserRepository;
        this.eventPublisher = eventPublisher;
    }

    public Optional<RegisteredUser> findUser(String email) {
        Preconditions.checkArgument(email != null, "email must not be null");
        return registeredUserRepository.findByEmail(email);
    }

    public ConfirmationToken registerUser(String name, String email, CharSequence rawPassword) {
        Preconditions.checkArgument(email != null, "email must not be null");
        Preconditions.checkArgument(rawPassword != null, "rawPassword must not be null");

        final Optional<RegisteredUser> existing = registeredUserRepository.findByEmail(email);
        if (existing.isPresent()) {
            // TODO: if user exists and is unconfirmed, maybe send confirmation mail
            // again?
            throw new RegisterFailedException(email);
        }
        final LocalDateTime nowUTC = dateTimeProvider.getNowLocal();
        final String cryptedPassword = passwordEncoder.encode(rawPassword);

        final Password password = Password.create(cryptedPassword, nowUTC);
        final ConfirmationToken confirmationToken = ConfirmationToken.randomValidFor(Duration.ofDays(1), nowUTC);

        eventPublisher.publishEvent(UserRegisteredEvent.create(name, confirmationToken.token(), email));

        final RegisteredUser newUser = RegisteredUser.newUser(name, email, password, confirmationToken, nowUTC);
        registeredUserRepository.save(newUser);
        return confirmationToken;
    }

    public RegisteredUser confirmRegistration(String confirmationToken) {
        Preconditions.checkArgument(confirmationToken != null, "confirmationToken must not be null");

        final RegisteredUser user = registeredUserRepository
                .findByRegistrationConfirmation_confirmationToken_token(confirmationToken)
                .orElseThrow(() -> new ConfirmationFailedException("confirmation token invalid/expired"));

        final LocalDateTime nowUTC = dateTimeProvider.getNowLocal();
        return registeredUserRepository.save(user.confirmRegistration(confirmationToken, nowUTC));
    }

    public ConfirmationToken requestResetPassword(String email) {
        Preconditions.checkArgument(email != null, "email must not be null");

        // TODO: throw different exception
        final RegisteredUser registeredUser = registeredUserRepository.findByEmail(email).orElseThrow();
        final LocalDateTime nowUTC = dateTimeProvider.getNowLocal();
        final ConfirmationToken confirmationToken = ConfirmationToken.randomValidFor(Duration.ofHours(2), nowUTC);

        eventPublisher.publishEvent(ResetPasswordRequestEvent.create(
                registeredUser.name(), confirmationToken.token(), email));

        registeredUserRepository.save(registeredUser.requestResetPassword(confirmationToken));
        return confirmationToken;
    }

    public RegisteredUser confirmResetPassword(String confirmationToken, CharSequence newRawPassword) {
        Preconditions.checkArgument(confirmationToken != null, "confirmationToken must not be null");
        Preconditions.checkArgument(newRawPassword != null, "newRawPassword must not be null");

        final RegisteredUser registeredUser = registeredUserRepository
                .findByResetPasswordConfirmation_confirmationToken_token(confirmationToken)
                .orElseThrow(() -> new ConfirmationFailedException("confirmation token invalid/expired"));

        final LocalDateTime nowUTC = dateTimeProvider.getNowLocal();
        final String newCryptedPassword = passwordEncoder.encode(newRawPassword);
        final Password newPassword = Password.create(newCryptedPassword, nowUTC);
        return registeredUserRepository
                .save(registeredUser.confirmResetPassword(confirmationToken, nowUTC, newPassword));
    }

    public LoginAttempt changePassword(LoginRequest loginRequest, CharSequence newRawPassword) {
        return registeredUserRepository.findByEmail(loginRequest.email())
                .map(registeredUser -> registeredUser.tryLogin(loginRequest, passwordEncoder))
                .or(() -> {
                    // Mitigate timing attack to find out whether a user might exist
                    passwordEncoder.encode("randomString");
                    return Optional.of(LoginAttempt.failed(loginRequest, LoginFailedReason.USER_DOSENT_EXIST));
                })
                .map(loginAttempt -> {
                    if (loginAttempt.isSuccessful()) {
                        final LocalDateTime nowUTC = dateTimeProvider.getNowLocal();
                        final String newCryptedPassword = passwordEncoder.encode(newRawPassword);
                        final Password newPassword = Password.create(newCryptedPassword, nowUTC);
                        final RegisteredUser user = loginAttempt.registeredUser();
                        registeredUserRepository.save(user.changePassword(newPassword));
                    }
                    return loginAttempt;
                })
                .orElseThrow(IllegalStateException::new);
    }

    public LoginAttempt login(LoginRequest loginRequest) {
        Preconditions.checkArgument(loginRequest != null, "loginRequest must not be null");
        return registeredUserRepository.findByEmail(loginRequest.email())
                .map(registeredUser -> registeredUser.tryLogin(loginRequest, passwordEncoder))
                .orElseGet(() -> LoginAttempt.failed(loginRequest, LoginFailedReason.USER_DOSENT_EXIST));
    }

    public void blockUser(String email, String reason, Duration duration) {
        final RegisteredUser registeredUser = registeredUserRepository.findByEmail(email)
                .orElseThrow();
        final LocalDateTime nowUTC = dateTimeProvider.getNowLocal();
        final LocalDateTime blockedUntilUTC = nowUTC.plus(duration);
        registeredUser.blockUntil(blockedUntilUTC, nowUTC, reason);
        registeredUserRepository.save(registeredUser);
    }
}
