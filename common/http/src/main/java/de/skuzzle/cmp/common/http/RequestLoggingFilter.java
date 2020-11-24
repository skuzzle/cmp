package de.skuzzle.cmp.common.http;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Iterables;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private void doFilterWithLogging(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            final long duration = System.currentTimeMillis() - start;
            final long bodySize = getBodySize(response);
            final int contentLength = request.getContentLength();
            final StringBuilder logMessage = new StringBuilder()
                    .append(response.getStatus()).append(" ")
                    .append(contentLength >= 0 ? contentLength + " " : "")
                    .append(request.getMethod()).append(" ").append(getUri(request))
                    .append(" ").append(bodySize)
                    .append(" ").append(duration).append("ms");

            if (LOGGER.isTraceEnabled()) {
                logMessage
                        .append(System.lineSeparator())
                        .append("RemoteAddr").append(": ").append(request.getRemoteAddr())
                        .append(System.lineSeparator())
                        .append("RemoteHost").append(": ").append(request.getRemoteHost())
                        .append(System.lineSeparator())
                        .append("Content-Length").append(": ").append(request.getContentLength())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());

                logMessage.append("Request Headers: ").append(System.lineSeparator());
                request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                    final Enumeration<String> headerValues = request.getHeaders(headerName);
                    final String headerValueString = getHeaderValueString(headerValues::asIterator);
                    logMessage.append(headerName).append(": ").append(headerValueString)
                            .append(System.lineSeparator());
                });

                logMessage.append("Response Headers: ").append(System.lineSeparator());
                response.getHeaderNames().iterator().forEachRemaining(headerName -> {
                    final Collection<String> headerValues = response.getHeaders(headerName);
                    final String headerValueString = getHeaderValueString(headerValues);
                    logMessage.append(headerName).append(": ").append(headerValueString)
                            .append(System.lineSeparator());
                });
            }

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Request: {}", logMessage);
            } else {
                LOGGER.debug("Request: {}", logMessage);
            }
        }
    }

    private void doFilterWithoutLogging(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (LOGGER.isDebugEnabled()) {
            doFilterWithLogging(request, response, filterChain);
        } else {
            doFilterWithoutLogging(request, response, filterChain);
        }

    }

    private long getBodySize(ServletResponse response) {
        if (response instanceof ResponseFacade) {
            final ResponseFacade rspo = (ResponseFacade) response;
            return rspo.getContentWritten();
        }
        return -1L;
    }

    private String getUri(HttpServletRequest request) {
        final StringBuffer requestURL = request.getRequestURL();
        final String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        }
        return requestURL.append("?").append(queryString).toString();
    }

    private String getHeaderValueString(Iterable<String> values) {
        final String[] headerValues = Iterables.toArray(values, String.class);
        return Arrays.stream(headerValues)
                .collect(Collectors.joining(", "));
    }

}
