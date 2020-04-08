package de.skuzzle.cmp.common.ratelimit;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

public class RemoteIpClientIdentificator implements ClientIdentificator<HttpServletRequest> {

    private static final Logger log = LoggerFactory.getLogger(RemoteIpClientIdentificator.class);

    @Override
    public Optional<ApiClient> tryIdentifyClientFrom(HttpServletRequest request) {
        final String remoteIp = getClientIP(request);
        return Optional.of(new SimpleApiClient(remoteIp));
    }

    private String getClientIP(HttpServletRequest request) {
        final String xForwardedFor = request.getHeader("X-Forwarded-For");
        final String xRealIp = request.getHeader("X-Real-Ip");
        final String remoteAddr = request.getRemoteAddr();

        log.debug("Trying to determine remote ip. X-Real-Ip: {}, X-Forwarded-For: {}, RemoteAddr: {}", xRealIp,
                xForwardedFor, remoteAddr);

        return Iterables.find(Arrays.asList(xRealIp, xForwardedFor, remoteAddr), Predicates.notNull());
    }

}
