package de.skuzzle.cmp.counter.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.google.common.net.HttpHeaders;

import de.skuzzle.cmp.common.http.RequestId;

class ClientIdInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ClientIdInterceptor.class);

    private final ClientId clientId;

    public ClientIdInterceptor(ClientId clientId) {
        this.clientId = clientId;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        RequestId.addToHeaders(request.getHeaders());
        request.getHeaders().add(RequestId.REQUEST_ID_HEADER, RequestId.forCurrentThread());
        request.getHeaders().add(ClientId.REAL_IP, clientId.getRealIp());
        request.getHeaders().add(HttpHeaders.X_FORWARDED_FOR, clientId.getForwardedFor());

        log.trace("Adding tracing information to backend request. X-Request-ID: {}, X-Real-Ip: {}, X-Forwarded-For: {}",
                clientId.getRequestId(), clientId.getRealIp(), clientId.getForwardedFor());

        clientId.getAccessToken().ifPresent(token -> {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        });

        return execution.execute(request, body);
    }

}
