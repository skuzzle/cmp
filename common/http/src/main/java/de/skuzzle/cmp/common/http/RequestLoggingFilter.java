package de.skuzzle.cmp.common.http;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Iterables;

public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!LOGGER.isTraceEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        final StringBuilder logMessage = new StringBuilder()
                .append(request.getMethod()).append(" ").append(getUri(request)).append(System.lineSeparator())
                .append("RemoteAddr").append(": ").append(request.getRemoteAddr()).append(System.lineSeparator())
                .append("RemoteHost").append(": ").append(request.getRemoteHost()).append(System.lineSeparator())
                .append("Content-Length").append(": ").append(request.getContentLength()).append(System.lineSeparator())
                .append(System.lineSeparator());

        logMessage.append("Headers: ").append(System.lineSeparator());
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            final Enumeration<String> headerValues = request.getHeaders(headerName);
            final String headerValueString = getHeaderValueString(headerValues);
            logMessage.append(headerName).append(": ").append(headerValueString)
                    .append(System.lineSeparator());
        });

        LOGGER.trace("Request:\n{}", logMessage);
        filterChain.doFilter(request, response);
    }

    private String getUri(HttpServletRequest request) {
        final StringBuffer requestURL = request.getRequestURL();
        final String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        }
        return requestURL.append("?").append(queryString).toString();
    }

    private String getHeaderValueString(Enumeration<String> values) {
        final String[] headerValues = Iterables.toArray(values::asIterator, String.class);
        return Arrays.stream(headerValues)
                .collect(Collectors.joining(", "));
    }

}
