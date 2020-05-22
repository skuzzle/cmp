package de.skuzzle.cmp.auth.security;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class AuthorizationCodeGrantFlow {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationCodeGrantFlow.class);

    private final TestRestTemplate restTemplate;
    private final String sessionCookieName;

    public AuthorizationCodeGrantFlow(TestRestTemplate restTemplate, String sessionCookieName) {
        this.restTemplate = restTemplate;
        this.sessionCookieName = sessionCookieName;
    }

    public AuthorizationCodeGrantFlow fromBrowser() {
        return this;
    }

    public AuthorizationCodeGrantFlow fromOAuthClient() {
        return this;
    }

    public AuthorizationServerResponse<?> initAuthorization(AuthorizationRequest request) {
        final ResponseEntity<String> responseEntity = get(String.class,
                "/oauth/authorize?" + request.toUriParameters());
        final AuthorizationServerResponse<String> authorizationServerResponse = new AuthorizationServerResponse<>(
                responseEntity, sessionCookieName);
        logger.info("Response to init authorization: {}", responseEntity);
        return authorizationServerResponse;
    }

    public AuthorizationServerResponse<?> login(String sessionId, String username, String password) {
        final ResponseEntity<String> responseEntity = postForm(String.class, "/login",
                Map.of("username", username, "password", password), sessionId);
        final AuthorizationServerResponse<String> authorizationServerResponse = new AuthorizationServerResponse<>(
                responseEntity, sessionCookieName);
        logger.info("Response to Login: {}", responseEntity);
        return authorizationServerResponse;
    }

    public AuthorizationServerResponse<?> finishAuthorization(String sessionId, AuthorizationRequest request) {
        final ResponseEntity<String> responseEntity = getWithSession(String.class,
                "/oauth/authorize?" + request.toUriParameters(), sessionId);
        final AuthorizationServerResponse<String> authorizationServerResponse = new AuthorizationServerResponse<>(
                responseEntity, sessionCookieName);
        logger.info("Response to finish authorization: {}", responseEntity);

        return authorizationServerResponse;
    }

    public AuthorizationServerResponse<OAuth2AccessToken> obtainAccessTokenFromCodeGrant(String authorizationCode,
            AuthorizationRequest request,
            String clientSecret) {
        final ResponseEntity<OAuth2AccessToken> responseEntity = postFormWithBasicAuth(OAuth2AccessToken.class,
                "/oauth/token", Map.of(
                        "grant_type", "authorization_code",
                        "response_type", "code",
                        "redirect_uri", request.redirectUri(),
                        "code", authorizationCode,
                        "client_id", request.clientId()),
                request.clientId(), clientSecret);
        final AuthorizationServerResponse<OAuth2AccessToken> authorizationServerResponse = new AuthorizationServerResponse<>(
                responseEntity, sessionCookieName);
        logger.info("Response to obtain access token: {}", responseEntity);

        return authorizationServerResponse;
    }

    public AuthorizationServerResponse<OAuth2AccessToken> obtainAccessTokenFromRefreshToken(String refreshToken,
            String clientId, String clientSecret) {
        final ResponseEntity<OAuth2AccessToken> responseEntity = postFormWithBasicAuth(OAuth2AccessToken.class,
                "/oauth/token", Map.of(
                        "grant_type", "refresh_token",
                        "refresh_token", refreshToken,
                        "client_id", clientId),
                clientId, clientSecret);
        final AuthorizationServerResponse<OAuth2AccessToken> authorizationServerResponse = new AuthorizationServerResponse<>(
                responseEntity, sessionCookieName);
        logger.info("Response to obtain new access token: {}", responseEntity);

        return authorizationServerResponse;
    }

    public AuthorizationServerResponse<String> obtainTokenInfo(OAuth2AccessToken accessToken, String clientId,
            String clientSecret) {
        final ResponseEntity<String> responseEntity = postFormWithBasicAuth(String.class,
                // NOTE: 'access_token' is the default string sent by Spring Boot OAuth2
                // client while 'token' is the string expected by Spring OAuth2
                // AuthorizationServer *wtf*
                "/oauth/check_token", Map.of("token", accessToken.getValue()),
                clientId, clientSecret);
        final AuthorizationServerResponse<String> authorizationServerResponse = new AuthorizationServerResponse<>(
                responseEntity, sessionCookieName);
        logger.info("Response to check access token: {}", responseEntity);
        return authorizationServerResponse;
    }

    private <T> ResponseEntity<T> postFormWithBasicAuth(Class<T> type, String url, Map<String, String> formData,
            String username,
            String password) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        formData.forEach((k, v) -> bodyMap.add(k, v));
        final HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(bodyMap, httpHeaders);
        return restTemplate
                .withBasicAuth(username, password)
                .postForEntity(url, body, type);
    }

    private <T> ResponseEntity<T> get(Class<T> type, String url, Object... params) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.ALL));
        final HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, entity, type, params);
    }

    private <T> ResponseEntity<T> getWithSession(Class<T> type, String url, String sessionId, Object... params) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", sessionCookieName + "=" + sessionId);
        httpHeaders.setAccept(List.of(MediaType.ALL));
        final HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, entity, type, params);
    }

    private <T> ResponseEntity<T> postForm(Class<T> type, String url, Map<String, String> formData, String sessionId) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Cookie", sessionCookieName + "=" + sessionId);

        final MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        formData.forEach((k, v) -> bodyMap.add(k, v));
        final HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(bodyMap, httpHeaders);
        return restTemplate.postForEntity(url, body, type);
    }

}
