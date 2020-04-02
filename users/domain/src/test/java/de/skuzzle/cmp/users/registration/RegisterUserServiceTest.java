package de.skuzzle.cmp.users.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

import de.skuzzle.cmp.common.time.UTCDateTimeProvider;
import de.skuzzle.cmp.users.mailnotification.SystemOutMailSender;

@SpringBootTest
public class RegisterUserServiceTest {

    @TestConfiguration
    static class RegisterUserServiceTestSpringConfiguration {
        @Bean
        @Primary
        public JavaMailSender sysoMailSender() {
            return new SystemOutMailSender();
        }
    }

    private final LocalDateTime now = UTCDateTimeProvider.getInstance().getNowLocal();

    @Autowired
    private RegisteredUserRepository userRepository;
    @Autowired
    private RegisterUserService registerUserService;

    @AfterEach
    void cleanupUsers() {
        userRepository.deleteAll();
    }

    @Nested
    class WithExistingUser {

        private ConfirmationToken confirmationToken;
        private final LoginRequest validLoginRequest = LoginRequest.create(now, "simon@taddiken.online", "123456");
        private final LoginRequest loginRequestWithWrongEmail = LoginRequest.create(now, "gibts@nicht.de", "123456");
        private final LoginRequest loginRequestWithWrongPassword = LoginRequest.create(now, "simon@taddiken.online",
                "falsch");

        @BeforeEach
        void setup() {
            confirmationToken = registerUserService.registerUser("Simon", "simon@taddiken.online", "123456");
        }

        @Test
        void testRegisterWithSameEmail() throws Exception {
            assertThatExceptionOfType(RegisterFailedException.class)
                    .isThrownBy(() -> registerUserService.registerUser("Peter", "simon@taddiken.online", "pwd"));
        }

        @Test
        void testLoginWithCorrectCredentialsButWithoutConfirmation() throws Exception {
            final LoginAttempt attempt = registerUserService.login(validLoginRequest);
            assertThat(attempt.isSuccessful()).isFalse();
            assertThat(attempt.reason()).isEqualTo(LoginFailedReason.USER_NOT_CONFIRMED);
        }

        @Test
        void testConfirmWithUnknownToken() throws Exception {
            assertThatExceptionOfType(ConfirmationFailedException.class)
                    .isThrownBy(() -> registerUserService.confirmRegistration("unknown token"));
        }

        @Nested
        class WithConfirmedUser {

            @BeforeEach
            void confirmRegistration() {
                registerUserService.confirmRegistration(confirmationToken.token());
            }

            @Test
            void testTryConfirmTwice() throws Exception {
                assertThatExceptionOfType(ConfirmationFailedException.class)
                        .isThrownBy(() -> registerUserService.confirmRegistration(confirmationToken.token()));
            }

            @Test
            void testLoginWithCorrectCredentials() throws Exception {
                final LoginAttempt attempt = registerUserService.login(validLoginRequest);
                assertThat(attempt.isSuccessful()).isTrue();
            }

            @Test
            void testTryLoginWithWrongEmail() throws Exception {
                final LoginAttempt attempt = registerUserService.login(loginRequestWithWrongEmail);
                assertThat(attempt.isSuccessful()).isFalse();
                assertThat(attempt.reason()).isEqualTo(LoginFailedReason.USER_DOSENT_EXIST);
            }

            @Test
            void testTryLoginWithWrongPassword() throws Exception {
                final LoginAttempt attempt = registerUserService.login(loginRequestWithWrongPassword);
                assertThat(attempt.isSuccessful()).isFalse();
                assertThat(attempt.reason()).isEqualTo(LoginFailedReason.PASSWORD_MISMATCH);
            }

            @Test
            void testTryLoginWithCorrectCredentialsButUserIsBlocked() throws Exception {
                registerUserService.blockUser("simon@taddiken.online", "Bad behavior", Duration.ofHours(2));
                final LoginAttempt attempt = registerUserService.login(validLoginRequest);
                assertThat(attempt.isSuccessful()).isFalse();
                assertThat(attempt.reason()).isEqualTo(LoginFailedReason.USER_BLOCKED);
            }

            @Test
            void testResetPassword() throws Exception {
                final ConfirmationToken resetPasswordToken = registerUserService
                        .requestResetPassword("simon@taddiken.online");

                registerUserService.confirmResetPassword(resetPasswordToken.token(), "newPassword");
                final LoginAttempt attempt = registerUserService
                        .login(LoginRequest.create(now, "simon@taddiken.online", "newPassword"));
                assertThat(attempt.isSuccessful()).isTrue();
            }

            @Test
            void testResetPasswordWithoutRequest() throws Exception {
                assertThatExceptionOfType(ConfirmationFailedException.class)
                        .isThrownBy(() -> registerUserService.confirmResetPassword("unknown token", "newPassword"));
            }

            @Test
            void testChangePassword() throws Exception {
                registerUserService.changePassword(validLoginRequest, "newPassword");
                final LoginAttempt attempt = registerUserService
                        .login(LoginRequest.create(now, "simon@taddiken.online", "newPassword"));
                assertThat(attempt.isSuccessful()).isTrue();
            }
        }

    }

    @Test
    void testRegisterUser() throws Exception {

    }

}
