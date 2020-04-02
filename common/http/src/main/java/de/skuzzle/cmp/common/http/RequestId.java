package de.skuzzle.cmp.common.http;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

public class RequestId {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_MDC = "requestId";

    public static String forCurrentThread() {
        String requestId = MDC.get(REQUEST_ID_MDC);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
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
            idFromRequest = UUID.randomUUID().toString();
        }
        MDC.put(REQUEST_ID_MDC, idFromRequest);
    }

    static void clearThread() {
        MDC.remove(REQUEST_ID_MDC);
    }
}
