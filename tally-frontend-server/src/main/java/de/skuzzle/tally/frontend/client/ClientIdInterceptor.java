package de.skuzzle.tally.frontend.client;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

class ClientIdInterceptor implements ClientHttpRequestInterceptor {

    private final ClientId clientId;

    public ClientIdInterceptor(ClientId clientId) {
        this.clientId = clientId;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        request.getHeaders().add("X-Request-ID", clientId.getRequestId());
        request.getHeaders().add("X-Real-IP", clientId.getRealIp());
        request.getHeaders().add("X-Forwarded-For", clientId.getForwardedFor());
        return execution.execute(request, body);
    }

}
