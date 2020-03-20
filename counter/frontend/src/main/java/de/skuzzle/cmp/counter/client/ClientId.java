package de.skuzzle.cmp.counter.client;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;

import de.skuzzle.cmp.auth.TallyUser;

@Component
class ClientId {

    private static final Logger log = LoggerFactory.getLogger(ClientId.class);

    static final String REQUEST_ID = "X-Request-ID";
    static final String REAL_IP = "X-Real-Ip";

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
        return Optional.ofNullable(request.getHeader(HttpHeaders.X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
    }

    public String getRequestId() {
        final String requestIdHeaderValue = request.getHeader(REQUEST_ID);
        if (requestIdHeaderValue != null) {
            log.trace("Found existing request id in HTTP header: {}", requestIdHeaderValue);
            return requestIdHeaderValue;
        }
        String requestId = (String) request.getAttribute(REQUEST_ID);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
            log.debug("Assigned new request id: {}", requestId);
            request.setAttribute(REQUEST_ID, requestId);
        }
        return requestId;
    }

    public String getRealIp() {
        return Optional.ofNullable(request.getHeader(REAL_IP))
                .orElseGet(request::getRemoteAddr);
    }

}
