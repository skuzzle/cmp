package de.skuzzle.cmp.turbolinks;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Strategy for determining the <code>Turbolinks-Location</code> header after a server
 * side redirect.
 *
 * @author Simon Taddiken
 */
public interface TurbolinksLocationStrategy {

    /**
     * Inspects the given request and determines which value to set for the
     * <code>Turbolinks-Location</code> header. If this method returns a non-empty
     * Optional, that value will be set as header on the currently processed servlet
     * response.
     *
     * @param request The current request.
     * @return The value to set as Turbolinks-Location
     */
    Optional<String> getTurbolinksLocation(HttpServletRequest request);

    String processRedirect(HttpServletRequest request, String redirectLocation);

}