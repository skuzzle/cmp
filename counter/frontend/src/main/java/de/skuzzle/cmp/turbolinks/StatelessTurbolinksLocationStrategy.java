package de.skuzzle.cmp.turbolinks;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Strategy tha
 *
 * @author Simon Taddiken
 */
public class StatelessTurbolinksLocationStrategy implements TurbolinksLocationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(StatelessTurbolinksLocationStrategy.class);

    private static final String REQUEST_PARAMETER_NAME = "tll";

    public static final TurbolinksLocationStrategy INSTANCE = new StatelessTurbolinksLocationStrategy();

    @Override
    public Optional<String> getTurbolinksLocation(HttpServletRequest request) {
        final String ttl = request.getParameter(REQUEST_PARAMETER_NAME);
        if (!"1".equals(ttl)) {
            return Optional.empty();
        }

        final String result = UriComponentsBuilder
                .fromHttpRequest(new ServletServerHttpRequest(request))
                .replaceQueryParam(REQUEST_PARAMETER_NAME)
                .build()
                .toString();
        logger.debug("Determined Turbolinks-Location from request param: {}", result);
        return Optional.of(result);
    }

    @Override
    public String processRedirect(HttpServletRequest request, String redirectLocation) {
        return appendTllRequestParamTo(redirectLocation);
    }

    private String appendTllRequestParamTo(String url) {
        final boolean hasQuestion = url.lastIndexOf('?') >= 0;
        final String sep = hasQuestion ? "&" : "?";
        return url + sep + REQUEST_PARAMETER_NAME + "=1";
    }
}
