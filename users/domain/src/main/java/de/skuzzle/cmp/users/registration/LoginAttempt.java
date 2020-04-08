package de.skuzzle.cmp.users.registration;

import com.google.common.base.Preconditions;

public class LoginAttempt {

    private final LoginRequest loginRequest;
    private final RegisteredUser registeredUser;
    private final LoginFailedReason loginFailedReason;

    private LoginAttempt(LoginRequest loginRequest, RegisteredUser registeredUser,
            LoginFailedReason loginFailedReason) {
        this.loginRequest = loginRequest;
        this.registeredUser = registeredUser;
        this.loginFailedReason = loginFailedReason;
    }

    public static LoginAttempt successful(LoginRequest request, RegisteredUser registeredUser) {
        Preconditions.checkArgument(request != null, "request must not be null");
        Preconditions.checkArgument(registeredUser != null, "registeredUser must not be null");
        return new LoginAttempt(request, registeredUser, null);
    }

    public static LoginAttempt failed(LoginRequest request, LoginFailedReason reason) {
        Preconditions.checkArgument(request != null, "request must not be null");
        Preconditions.checkArgument(reason != null, "reason must not be null");
        return new LoginAttempt(request, null, reason);
    }

    public RegisteredUser registeredUser() {
        Preconditions.checkArgument(isSuccessful(), "Can not get user from failed login attempt");
        return this.registeredUser;
    }

    public boolean isSuccessful() {
        return registeredUser != null;
    }

    public LoginRequest loginRequest() {
        return this.loginRequest;
    }

    public LoginFailedReason reason() {
        Preconditions.checkArgument(!isSuccessful(), "Can not get failed reason from successful login attempt");
        return this.loginFailedReason;
    }
}
