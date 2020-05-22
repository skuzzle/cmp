package de.skuzzle.cmp.common.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Preconditions;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseSizeTrackingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseSizeTrackingFilter.class);
    private int responseSizeWarningThreshold = Integer.MAX_VALUE;

    public void setResponseSizeWarningThreshold(int responseSizeWarningThreshold) {
        Preconditions.checkArgument(responseSizeWarningThreshold > 0, "threshold must be > 0");
        this.responseSizeWarningThreshold = responseSizeWarningThreshold;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (response instanceof ResponseFacade) {
                final ResponseFacade rspo = (ResponseFacade) response;
                final long contentWritten = rspo.getContentWritten();
                LOGGER.trace("{} bytes sent as response to {} {}", contentWritten, request.getMethod(),
                        request.getRequestURL());
                if (contentWritten > responseSizeWarningThreshold) {
                    LOGGER.warn("Response for request to {} {} exceed threshold of {} bytes. Response size: {}",
                            request.getMethod(), request.getRequestURI(), responseSizeWarningThreshold, contentWritten);
                }
                Metrics.counter("http_server_response_bytes",
                        List.of(Tag.of("uri", request.getRequestURI()))).increment(contentWritten);
            }
        }
    }

}
