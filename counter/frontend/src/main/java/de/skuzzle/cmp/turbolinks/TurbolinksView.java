package de.skuzzle.cmp.turbolinks;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

class TurbolinksView extends RedirectView {

    private static final String RESPONSE_BODY_TEMPLATE = "" +
            "Turbolinks.clearCache()\n" +
            "Turbolinks.visit(\"%s\", %s)"; // params: location, action json

    private final TurbolinksLocationStrategy locationStrategy = StatelessTurbolinksLocationStrategy.INSTANCE;
    private final RedirectOptions options;

    public TurbolinksView(String url, RedirectOptions options) {
        super(url);
        setExposeModelAttributes(false);
        this.options = options;
    }

    @Override
    public boolean isRedirectView() {
        return !options.respondWithXhrJavaScript();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final String turboLinksReferrer = request.getHeader("Turbolinks-Referrer");

        if (options.respondWithXhrJavaScript()) {
            // visit with tl

            String location = createTargetUrl(model, request);
            location = updateTargetUrl(location, model, request, response);

            // Save flash attributes
            RequestContextUtils.saveOutputFlashMap(location, request, response);

            response.setStatus(200);
            response.setContentType("text/javascript");
            response.addHeader("X-Xhr-Redirect", location);

            final ServletOutputStream outputStream = response.getOutputStream();
            try (final PrintWriter writer = new PrintWriter(outputStream, false, StandardCharsets.UTF_8)) {
                writer.printf(RESPONSE_BODY_TEMPLATE, location, options.visitAction().toTurbolinksJsonString());
            }

            return;
        } else if (turboLinksReferrer != null) {
            final String updatedRedirectLocation = locationStrategy.processRedirect(request, getUrl());
            setUrl(updatedRedirectLocation);
        }

        super.renderMergedOutputModel(model, request, response);
    }

}
