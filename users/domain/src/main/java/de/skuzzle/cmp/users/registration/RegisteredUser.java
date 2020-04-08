package de.skuzzle.cmp.users.registration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.base.Preconditions;

public class RegisteredUser {

    @Id
    private String id;
    @Version
    private int version;

    private final String name;

    @Indexed
    private final String email;
    private Password password;

    private Confirmation registrationConfirmation;
    private Confirmation resetPasswordConfirmation;
    private final LocalDateTime registrationTimeUTC;
    private final Set<String> authorities;
    private Block block;

    private RegisteredUser(String name, String email, Password password,
            Confirmation registrationConfirmation,
            Confirmation resetPasswordConfirmation,
            LocalDateTime registrationTimeUTC,
            Set<String> authorities,
            Block block) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.registrationConfirmation = registrationConfirmation;
        this.resetPasswordConfirmation = resetPasswordConfirmation;
        this.registrationTimeUTC = registrationTimeUTC;
        this.authorities = authorities;
        this.block = block;
    }

    public static RegisteredUser newUser(String name, String email, Password password,
            ConfirmationToken registrationConfirmationToken,
            LocalDateTime registrationTimeUTC) {
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(email != null, "email must not be null");
        Preconditions.checkArgument(password != null, "password must not be null");
        Preconditions.checkArgument(registrationConfirmationToken != null,
                "registrationConfirmationToken must not be null");
        Preconditions.checkArgument(registrationTimeUTC != null, "registrationTimeUTC must not be null");

        final Confirmation registrationConfirmation = Confirmation.unconfirmed(registrationConfirmationToken);
        final Confirmation dummyResetPasswordConfirmation = Confirmation.confirmed(registrationTimeUTC);
        return new RegisteredUser(name, email, password,
                registrationConfirmation,
                dummyResetPasswordConfirmation,
                registrationTimeUTC,
                new HashSet<>(),
                null);
    }

    public LocalDateTime registrationTimeUTC() {
        return this.registrationTimeUTC;
    }

    public String name() {
        return this.name;
    }

    public Password password() {
        return this.password;
    }

    public Set<String> authorities() {
        return Collections.unmodifiableSet(authorities);
    }

    public Confirmation registrationConfirmation() {
        return this.registrationConfirmation;
    }

    public RegisteredUser confirmRegistration(String expectedToken, LocalDateTime confirmationTimeUTC) {
        Preconditions.checkState(!this.registrationConfirmation.isConfirmed(), "Registration is already confirmed");
        this.registrationConfirmation = registrationConfirmation.confirm(expectedToken, confirmationTimeUTC);
        return this;
    }

    public RegisteredUser requestResetPassword(ConfirmationToken confirmationToken) {
        this.resetPasswordConfirmation = Confirmation.unconfirmed(confirmationToken);
        return this;
    }

    public RegisteredUser confirmResetPassword(String expectedToken, LocalDateTime confirmationTimeUTC,
            Password newPassword) {
        Preconditions.checkArgument(newPassword != null, "newPassword must not be null");
        this.resetPasswordConfirmation = resetPasswordConfirmation.confirm(expectedToken, confirmationTimeUTC);
        this.password = newPassword;
        return this;
    }

    public RegisteredUser changePassword(Password newPassword) {
        this.password = newPassword;
        return this;
    }

    public LoginAttempt tryLogin(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        if (!registrationConfirmation.isConfirmed()) {
            return LoginAttempt.failed(loginRequest, LoginFailedReason.USER_NOT_CONFIRMED);
        } else if (block != null && block.isBlocked(loginRequest.loginRequestTimeUTC())) {
            return LoginAttempt.failed(loginRequest, LoginFailedReason.USER_BLOCKED);
        }
        final boolean passwordMatches = passwordEncoder.matches(
                loginRequest.rawPassword(),
                password.hashedPassword());

        if (!passwordMatches) {
            return LoginAttempt.failed(loginRequest, LoginFailedReason.PASSWORD_MISMATCH);
        }
        return LoginAttempt.successful(loginRequest, this);
    }

    public RegisteredUser blockUntil(LocalDateTime blockedUntilUTC, LocalDateTime nowUTC, String reason) {
        this.block = Block.until(blockedUntilUTC, nowUTC, reason);
        return this;
    }
}
