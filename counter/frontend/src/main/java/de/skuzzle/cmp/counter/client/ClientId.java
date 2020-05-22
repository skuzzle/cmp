package de.skuzzle.cmp.counter.client;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.common.http.RequestId;

@Component
class ClientId {

    static final String REAL_IP = "X-Real-Ip";

    private final HttpServletRequest request;
    private final TallyUser tallyUser;

    public ClientId(TallyUser tallyUser, HttpServletRequest request) {
        this.tallyUser = tallyUser;
        this.request = request;
    }

    public Optional<String> getAccessToken() {
        return tallyUser.getAccessToken();
    }

    public String getForwardedFor() {
        return Optional.ofNullable(request.getHeader(HttpHeaders.X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
    }

    public String getRequestId() {
        return RequestId.forCurrentThread();
    }

    public String getRealIp() {
        return Optional.ofNullable(request.getHeader(REAL_IP))
                .orElseGet(request::getRemoteAddr);
    }

}
