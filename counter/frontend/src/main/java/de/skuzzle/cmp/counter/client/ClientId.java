package de.skuzzle.cmp.counter.client;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import de.skuzzle.cmp.auth.TallyUser;

@Component
class ClientId {

    static final String REQUEST_ID = "X-Request-ID";
    static final String REAL_IP = "X-Real-IP";
    static final String FORWARTED_FOR = "X-Forwarded-For";

    private final HttpServletRequest request;
    private final TallyUser tallyUser;

    public ClientId(TallyUser tallyUser, HttpServletRequest request) {
        this.tallyUser = tallyUser;
        this.request = request;
    }

    public Optional<String> getOidToken() {
        return tallyUser.getOidToken();
    }

    public String getForwardedFor() {
        return Optional.ofNullable(request.getHeader(FORWARTED_FOR))
                .map(header -> header.split(",")[0])
                .orElseGet(request::getRemoteAddr);
    }

    public String getRequestId() {
        final String requestIdHeaderValue = request.getHeader(REQUEST_ID);
        if (requestIdHeaderValue != null) {
            return requestIdHeaderValue;
        }
        String requestId = (String) request.getAttribute(REQUEST_ID);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
            request.setAttribute(REQUEST_ID, requestId);
        }
        return requestId;
    }

    public String getRealIp() {
        return Optional.ofNullable(request.getHeader(REAL_IP))
                .orElseGet(request::getRemoteAddr);
    }

}
