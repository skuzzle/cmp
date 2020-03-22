package de.skuzzle.cmp.counter.urls;

import org.springframework.web.util.UriTemplate;

import com.google.common.base.Preconditions;

public enum KnownUrls {
    VIEW_COUNTER(KnownUrls.VIEW_COUNTER_STRING),
    INCREMENT_COUNTER(KnownUrls.INCREMENT_COUNTER_STRING);

    public static final String BASE_URL = "/counter";
    public static final String VIEW_COUNTER_STRING = BASE_URL + "/{key}";
    public static final String INCREMENT_COUNTER_STRING = BASE_URL + "/{key}/increment/{incrementId}";

    private final UriTemplate template;

    private KnownUrls(String templateString) {
        Preconditions.checkArgument(templateString != null, "templateString must not be null");
        this.template = new UriTemplate(templateString);
    }

    public String resolve(Object... params) {
        return this.template.expand(params).toString();
    }

    public String redirectResolve(Object... params) {
        return "redirect:" + resolve(params);
    }
}
