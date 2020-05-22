package de.skuzzle.cmp.auth.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthorizationServerResponse<T> {

    private final ResponseEntity<T> response;
    private final String sessionCookieName;

    AuthorizationServerResponse(ResponseEntity<T> response, String sessionCookieName) {
        this.response = response;
        this.sessionCookieName = sessionCookieName;
    }

    public String getAuthorizationCode() {
        final String location = getFirstHeader("Location");
        final int start = location.lastIndexOf("code=") + "code=".length();
        if (start < 0) {
            throw new IllegalStateException("Location header doesn contain 'code=' parameter: " + location);
        }
        final int end = location.indexOf('&', start);
        return location.substring(start, end < 0 ? location.length() : end);
    }

    public T getBody() {
        return response.getBody();
    }

    public AuthorizationServerResponse<T> expectRedirectTo(String uri) {
        expectStatus(HttpStatus.FOUND);
        assertThat(getFirstHeader("Location")).isEqualTo(uri);
        return this;
    }

    public AuthorizationServerResponse<T> expectRedirectMatching(String pattern) {
        expectStatus(HttpStatus.FOUND);
        assertThat(getFirstHeader("Location")).matches(pattern);
        return this;
    }

    String getSessionId() {
        List<String> setCookieValues = response.getHeaders().get("Set-Cookie");
        if (setCookieValues == null) {
            setCookieValues = Collections.emptyList();
        }
        final String sessionIdCookie = setCookieValues.stream()
                .filter(value -> value.startsWith(sessionCookieName + "="))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "Response has no Set-Cookie header for '%s': %s", sessionCookieName, response)));

        // JSESSIONID=A108F117A3E91789AF02D2A5882BFB2D; Path=/; HttpOnly
        int semi = sessionIdCookie.indexOf(';');
        semi = semi < 0 ? sessionIdCookie.length() : semi;
        return sessionIdCookie.substring(sessionIdCookie.indexOf('=') + 1, semi);
    }

    public AuthorizationServerResponse<T> expectStatus(HttpStatus status) {
        assertThat(response.getStatusCode())
                .as("Response is expected to have status %s. Response was: %s", status, response)
                .isEqualTo(status);
        return this;
    }

    private String getFirstHeader(String headerName) {
        final List<String> values = response.getHeaders().get(headerName);
        if (values == null || values.isEmpty() || values.get(0).isEmpty()) {
            throw new IllegalStateException("Response should contain a value for HTTP Header: " + headerName);
        }
        return values.get(0);
    }

}
