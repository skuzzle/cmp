package de.skuzzle.cmp.users.security;

import static de.skuzzle.cmp.users.security.ResponseEntityAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.skuzzle.cmp.users.registration.ConfirmationToken;
import de.skuzzle.cmp.users.registration.RegisterFailedException;
import de.skuzzle.cmp.users.registration.RegisterUserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OAuthEndToEndTest {

    @TestConfiguration
    static class RegisterUserServiceTestSpringConfiguration {
        @Bean
        @Primary
        public JavaMailSender sysoMailSender() {
            return new SystemOutMailSender();
        }
    }

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

    @Test
    void testPasswordFlow() throws Exception {
        final Map<String, String> params = Map.of(
                "grant_type", "password",
                // "client_id", "cmp",
                // "client_secret", "abc",
                "scope", "resource:read",
                "username", "confirmed@mail.com",
                "password", "secret");
        final ResponseEntity<?> loginResponse = postFormWithBasicAuth("/oauth/token", params, "cmp", "abc");
        assertThat(loginResponse).hasStatus(HttpStatus.OK);
    }

    @Test
    void testLoginFlow() throws Exception {
        // Initialize authorization
        final ResponseEntity<?> initAuthResponse = get(
                "/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=cmp&state=1234");

        assertThat(initAuthResponse).isRedirectTo(localurl + "/login");
        assertThat(initAuthResponse).hasHeader("Set-Cookie").anyMatch(s -> s.startsWith("JSESSIONID="));

        final String sessionId = getSessionId(initAuthResponse);
        // login
        final var credentials = Map.of(
                "username", "confirmed@mail.com",
                "password", "secret");
        final ResponseEntity<?> loginResponse = postForm("/login", credentials,
                sessionId);
        assertThat(loginResponse).isRedirectTo(localurl
                + "/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=cmp&state=1234");
    }

    private String getSessionId(ResponseEntity<?> response) {
        final String sessionIdCookie = response.getHeaders()
                .get("Set-Cookie").stream().filter(s -> s.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // JSESSIONID=A108F117A3E91789AF02D2A5882BFB2D; Path=/; HttpOnly
        int semi = sessionIdCookie.indexOf(';');
        semi = semi < 0 ? sessionIdCookie.length() : semi;
        return sessionIdCookie.substring(sessionIdCookie.indexOf('=') + 1, semi);
    }

    private ResponseEntity<String> postFormWithBasicAuth(String url, Map<String, String> formData, String username,
            String password) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        formData.forEach((k, v) -> bodyMap.add(k, v));
        final HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(bodyMap, httpHeaders);
        return restTemplate
                .withBasicAuth(username, password)
                .postForEntity(url, body, String.class);
    }

    private ResponseEntity<String> postForm(String url, Map<String, String> formData, String sessionId) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Cookie", "JSESSIONID=" + sessionId);

        final MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        formData.forEach((k, v) -> bodyMap.add(k, v));
        final HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(bodyMap, httpHeaders);
        return restTemplate.postForEntity(url, body, String.class);
    }

    private ResponseEntity<String> get(String url, Object... params) {
        return restTemplate.getForEntity(url, String.class, params);
    }

}
