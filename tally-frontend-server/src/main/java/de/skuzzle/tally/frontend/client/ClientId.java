package de.skuzzle.tally.frontend.client;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
class ClientId {

    static final String REQUEST_ID = "X-Request-ID";

    private final HttpServletRequest request;

    public ClientId(HttpServletRequest request) {
        this.request = request;
    }

    public String getForwardedFor() {
        return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
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
        return Optional.ofNullable(request.getHeader("X-Real-IP"))
                .orElseGet(request::getRemoteAddr);
    }

}
