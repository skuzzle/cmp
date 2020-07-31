package de.skuzzle.cmp.turbolinks;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter observes incoming requests and decides whether to set the
 * <code>Turbolinks-Location</code> header on the response. The header will be set to the
 * full absolute uri of the current request.
 *
 * @author Simon Taddiken
 */
@Component
public class TurbolinksLocationFilter extends OncePerRequestFilter {

    private final TurbolinksLocationStrategy strategy = StatelessTurbolinksLocationStrategy.INSTANCE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        strategy.getTurbolinksLocation(request)
                .ifPresent(turbolinksLocation -> response.setHeader("Turbolinks-Location", turbolinksLocation));

        filterChain.doFilter(request, response);
    }

}
