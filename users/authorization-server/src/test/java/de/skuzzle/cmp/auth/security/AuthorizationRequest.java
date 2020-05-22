package de.skuzzle.cmp.auth.security;

public class AuthorizationRequest {

    private final String grantType = "authorization_code";
    private final String responseType = "code";
    private String clientId;
    private String state;
    private String redirectUri;
    private String scope;

    public AuthorizationRequest withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String clientId() {
        return clientId;
    }

    public AuthorizationRequest withState(String state) {
        this.state = state;
        return this;
    }

    public AuthorizationRequest withRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String redirectUri() {
        return redirectUri;
    }

    public AuthorizationRequest withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String toUriParameters() {
        return String.format("grant_type=%s&response_type=%s&client_id=%s&state=%s&redirect_uri=%s&scope=%s",
                urlEncode(grantType),
                urlEncode(responseType),
                urlEncode(clientId),
                urlEncode(state),
                urlEncode(redirectUri),
                urlEncode(scope));
    }

    private String urlEncode(String s) {
        return s;
    }

}
