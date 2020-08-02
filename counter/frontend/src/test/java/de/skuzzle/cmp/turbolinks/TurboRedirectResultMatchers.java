package de.skuzzle.cmp.turbolinks;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.util.UriComponentsBuilder;

public class TurboRedirectResultMatchers {

    public static ResultMatcher turboRedirectToUrl(String url) {
        return result -> result.getResponse().getContentAsString().contains("Turbolinks.visit(\"" + url);
    }

    public static ResultMatcher turboRedirectToUrlTemplate(String template, Object... uriVars) {
        final String uri = UriComponentsBuilder.fromUriString(template).buildAndExpand(uriVars).encode()
                .toUriString();
        return turboRedirectToUrl(uri);
    }

}
