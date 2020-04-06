package de.skuzzle.cmp.common.http;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import de.skuzzle.cmp.common.random.RandomKey;

public final class RequestId {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_MDC = "requestId";

    private RequestId() {
        // hidden
    }

    public static String forCurrentThread() {
        String requestId = MDC.get(REQUEST_ID_MDC);
        if (requestId == null) {
            requestId = RandomKey.randomUUID();
            MDC.put(REQUEST_ID_MDC, requestId);
        }
        return requestId;
    }

    public static HttpHeaders addToHeaders(HttpHeaders headerMap) {
        headerMap.add(REQUEST_ID_HEADER, forCurrentThread());
        return headerMap;
    }

    static void updateThreadFrom(HttpServletRequest request) {
        String idFromRequest = request.getHeader(REQUEST_ID_HEADER);
        if (idFromRequest == null) {
            idFromRequest = RandomKey.randomUUID();
        }
        MDC.put(REQUEST_ID_MDC, idFromRequest);
    }

    static void clearThread() {
        MDC.remove(REQUEST_ID_MDC);
    }
}
