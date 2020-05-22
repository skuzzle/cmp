package de.skuzzle.cmp.auth.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import de.skuzzle.cmp.users.registration.ConfirmationToken;
import de.skuzzle.cmp.users.registration.RegisterFailedException;
import de.skuzzle.cmp.users.registration.RegisterUserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OAuthEndToEndTest {

    @LocalServerPort
    private int localPort;
    private String localurl;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RegisterUserService registerUserService;

    @BeforeEach
    void setupRegisteredUser() {
        this.localurl = "http://localhost:" + localPort;

        try {
            // valid, confirmed user
            final ConfirmationToken confirmationToken1 = registerUserService.registerUser("confirmedUser",
                    "confirmed@mail.com",
                    "secret");
            registerUserService.confirmRegistration(confirmationToken1.token());

            // blocked confirmed user
            final ConfirmationToken confirmationToken2 = registerUserService.registerUser("confirmedBlockedUser",
                    "confirmedBlocked@mail.com",
                    "secret");
            registerUserService.confirmRegistration(confirmationToken2.token());
            registerUserService.blockUser("confirmedBlocked@mail.com", "No reason", Duration.ofDays(10));

            // unconfirmed user
            registerUserService.registerUser("unconfirmed", "unconfirmed@mail.com", "secret");
        } catch (final RegisterFailedException e) {

        }
    }

    @Test
    void testJwksEndpointAvailable() throws Exception {
        assertThatCode(() -> restTemplate.getForObject("/.well-known/jwks.json", Map.class))
                .doesNotThrowAnyException();
    }

    private OAuth2AccessToken quickLogin(AuthorizationRequest request,
            String username, String password, String clientSecret) {
        final AuthorizationCodeGrantFlow flow = new AuthorizationCodeGrantFlow(restTemplate, "authorization");
        final AuthorizationServerResponse<?> initAuthorization = flow.initAuthorization(request);
        final AuthorizationServerResponse<?> login = flow.login(initAuthorization.getSessionId(), username, password);
        final AuthorizationServerResponse<?> finishAuthorization = flow.finishAuthorization(login.getSessionId(),
                request);
        return flow.obtainAccessTokenFromCodeGrant(finishAuthorization.getAuthorizationCode(), request, clientSecret)
                .expectStatus(HttpStatus.OK)
                .getBody();
    }

    @Nested
    class InitAuthorization {
        final AuthorizationRequest authorizationRequest = new AuthorizationRequest()
                .withClientId("cmp")
                .withState("randomState")
                .withRedirectUri("http://127.0.0.1:8081/login/oauth2/code/cmp")
                .withScope("read:user");

        private AuthorizationCodeGrantFlow flow;
        private AuthorizationServerResponse<?> initAuthorization;

        @BeforeEach
        void initAuthorization() {
            flow = new AuthorizationCodeGrantFlow(restTemplate, "authorization");
            initAuthorization = flow
                    .fromBrowser()
                    .initAuthorization(authorizationRequest);
        }

        @Test
        void testRedirectToLogin() throws Exception {
            initAuthorization.expectRedirectTo(localurl + "/login");
        }

        @Nested
        class Login {

            AuthorizationServerResponse<?> loginResponse;

            @BeforeEach
            void login() {
                loginResponse = flow
                        .fromBrowser()
                        .login(initAuthorization.getSessionId(), "confirmed@mail.com", "secret");
            }

            @Test
            void testRedirectToAuthorizationEndpointAfterLogin() {
                loginResponse.expectRedirectTo(localurl + "/oauth/authorize?" + authorizationRequest.toUriParameters());
            }

            @Test
            void testSessionIdChanged() throws Exception {
                assertThat(loginResponse.getSessionId()).isNotEqualTo(initAuthorization.getSessionId());
            }

            @Nested
            class FinishAuthorization {
                AuthorizationServerResponse<?> authorizationResponse;

                @BeforeEach
                void finishAuthorization() {
                    authorizationResponse = flow
                            .fromBrowser()
                            .finishAuthorization(loginResponse.getSessionId(), authorizationRequest);
                }

                @Test
                void testRedirectToOauthClientWithCode() throws Exception {
                    authorizationResponse.expectRedirectMatching(
                            "http://127.0.0.1:8081/login/oauth2/code/cmp\\?code=.*?&state=randomState");
                }

                @Nested
                class ObtainToken {
                    AuthorizationServerResponse<OAuth2AccessToken> accessTokenResponse;

                    @BeforeEach
                    void obtainAccessToken() {
                        accessTokenResponse = flow
                                .fromOAuthClient()
                                .obtainAccessTokenFromCodeGrant(authorizationResponse.getAuthorizationCode(),
                                        authorizationRequest,
                                        "abc");
                    }

                    @Test
                    void testSuccess() throws Exception {
                        accessTokenResponse.expectStatus(HttpStatus.OK);
                    }

                    @Test
                    void testContainsRefreshToken() throws Exception {
                        final OAuth2AccessToken token = accessTokenResponse.getBody();
                        assertThat(token.getRefreshToken()).as("AuthorizationServer didn't return a refresh token")
                                .isNotNull();
                    }

                    @Test
                    void testRetrieveTokenInfo() throws Exception {
                        final OAuth2AccessToken token = accessTokenResponse.getBody();
                        flow
                                .fromOAuthClient()
                                .obtainTokenInfo(token, "cmp", "abc")
                                .expectStatus(HttpStatus.OK);
                    }

                    @Test
                    void testRefreshToken() throws Exception {
                        final OAuth2AccessToken token = accessTokenResponse.getBody();
                        final String refreshToken = token.getRefreshToken().getValue();
                        final OAuth2AccessToken body = flow
                                .obtainAccessTokenFromRefreshToken(refreshToken, "cmp", "abc")
                                .expectStatus(HttpStatus.OK)
                                .getBody();

                        assertThat(body.isExpired()).isFalse();
                    }
                }
            }
        }

    }

    @Test
    void testAuthorizationGrantFlowSuccessfulLogin() throws Exception {
        final AuthorizationCodeGrantFlow flow = new AuthorizationCodeGrantFlow(restTemplate, "authorization");

        final AuthorizationRequest authorizationRequest = new AuthorizationRequest()
                .withClientId("cmp")
                .withState("randomState")
                .withRedirectUri("http://127.0.0.1:8081/login/oauth2/code/cmp")
                .withScope("read:user");

        final AuthorizationServerResponse<?> initAuthorization = flow
                .fromBrowser()
                .initAuthorization(authorizationRequest)
                .expectRedirectTo(localurl + "/login");

        final AuthorizationServerResponse<?> loginResponse = flow
                .fromBrowser()
                .login(initAuthorization.getSessionId(), "confirmed@mail.com", "secret")
                .expectRedirectTo(localurl + "/oauth/authorize?" + authorizationRequest.toUriParameters());

        final AuthorizationServerResponse<?> authorizationResponse = flow
                .fromBrowser()
                .finishAuthorization(loginResponse.getSessionId(), authorizationRequest)
                .expectRedirectMatching("http://127.0.0.1:8081/login/oauth2/code/cmp\\?code=.*?&state=randomState");

        final AuthorizationServerResponse<OAuth2AccessToken> accessTokenResponse = flow
                .fromOAuthClient()
                .obtainAccessTokenFromCodeGrant(authorizationResponse.getAuthorizationCode(), authorizationRequest,
                        "abc")
                .expectStatus(HttpStatus.OK);

        final OAuth2AccessToken token = accessTokenResponse.getBody();
        assertThat(token.getRefreshToken()).as("AuthorizationServer didn't return a refresh token").isNotNull();

        flow
                .fromOAuthClient()
                .obtainTokenInfo(token, "cmp", "abc")
                .expectStatus(HttpStatus.OK);
    }

}
