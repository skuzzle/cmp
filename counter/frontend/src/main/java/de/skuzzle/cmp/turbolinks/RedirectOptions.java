package de.skuzzle.cmp.turbolinks;

import com.google.common.base.Preconditions;

public class RedirectOptions {

    private VisitAction action = VisitAction.ADVANCE;
    private boolean xhrResponse = true;

    public static RedirectOptions defaultOptions() {
        return new RedirectOptions();
    }

    public RedirectOptions withAction(VisitAction action) {
        Preconditions.checkArgument(action != null, "action must not be null");
        this.action = action;
        return this;
    }

    public RedirectOptions withJavaScriptResponse() {
        xhrResponse = true;
        return this;
    }

    public boolean respondWithXhrJavaScript() {
        return this.xhrResponse;
    }

    public VisitAction visitAction() {
        return this.action;
    }
}
