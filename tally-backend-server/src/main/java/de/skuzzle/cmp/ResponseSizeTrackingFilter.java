package de.skuzzle.cmp;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ResponseFacade;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ResponseSizeTrackingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (response instanceof ResponseFacade) {
                final ResponseFacade rspo = (ResponseFacade) response;
                final long contentWritten = rspo.getContentWritten();
                Metrics.counter("http_server_response_bytes",
                        List.of(Tag.of("uri", request.getRequestURI()))).increment(contentWritten);
            }
        }
    }

}
