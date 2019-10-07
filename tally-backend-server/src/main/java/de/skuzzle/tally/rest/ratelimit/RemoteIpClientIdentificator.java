package de.skuzzle.tally.rest.ratelimit;

import javax.servlet.http.HttpServletRequest;

public class RemoteIpClientIdentificator implements ClientIdentificator<HttpServletRequest> {

    @Override
    public ApiClient identifyClientFrom(HttpServletRequest request) {
        final String remoteIp = getClientIP(request);
        return new SimpleApiClient(remoteIp);
    }

    private String getClientIP(HttpServletRequest request) {
        final String xrealIp = request.getHeader("X-Real-IP");
        if (xrealIp == null) {
            return request.getRemoteAddr();
        }
        return xrealIp;
    }

}
