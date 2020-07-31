package de.skuzzle.cmp.turbolinks;

import org.springframework.web.servlet.View;

import com.google.common.base.Preconditions;

public final class TurboRedirect {

    public static View to(String location) {
        return to(location, RedirectOptions.defaultOptions());
    }

    public static View to(String location, RedirectOptions options) {
        Preconditions.checkArgument(location != null, "location must not be null");
        Preconditions.checkArgument(options != null, "options must not be null");
        return new TurbolinksView(location, options);
    }

    private TurboRedirect() {
        // hidden
    }
}
