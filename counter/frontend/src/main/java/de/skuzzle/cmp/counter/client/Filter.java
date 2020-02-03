package de.skuzzle.cmp.counter.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import de.skuzzle.cmp.counter.urls.KnownUrls;

public class Filter {

    private LocalDate from;
    private LocalDate until;
    private Tags filterTags = Tags.none();

    public static Filter all() {
        return new Filter();
    }

    public boolean isActive() {
        return from != null || until != null || !filterTags.all().isEmpty();
    }

    public Filter from(LocalDate from) {
        this.from = from;
        return this;
    }

    public Filter until(LocalDate to) {
        this.until = to;
        return this;
    }

    public Filter withTags(Tags filterTags) {
        Preconditions.checkArgument(filterTags != null, "filterTags must not be null");
        this.filterTags = filterTags;
        return this;
    }

    public Filter removeTag(String name) {
        filterTags = filterTags.copyAndRemove(name);
        return this;
    }

    public Filter addTag(String name) {
        filterTags = filterTags.copyAndAdd(name);
        return this;
    }

    RestTallyResponse callBackendUsing(RestTemplate restTemplate, String publicKey) {
        final Map<String, String> filterParameters = buildParams();
        if (filterParameters.isEmpty()) {
            return restTemplate.getForObject("/{key}", RestTallyResponse.class, publicKey);
        }
        final String filterString = filterParameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        return restTemplate.getForObject("/{key}?" + filterString, RestTallyResponse.class, publicKey);

    }

    public String getCounterLink(String counterKey) {
        final String base = KnownUrls.VIEW_COUNTER.resolve(counterKey);
        final Map<String, String> filterParameters = buildParams();
        if (filterParameters.isEmpty()) {
            return base;
        }
        final String filterString = filterParameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        return base + "?" + filterString;
    }

    private Map<String, String> buildParams() {
        final Map<String, String> params = new LinkedHashMap<>();
        if (from != null) {
            params.put("from", from.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (until != null) {
            params.put("to", until.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (filterTags != null && !filterTags.all().isEmpty()) {
            params.put("tags", filterTags.toString());
        }
        return params;
    }

    public boolean containsTag(String name) {
        return filterTags.contains(name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Filter
                && Objects.equal(from, ((Filter) obj).from)
                && Objects.equal(until, ((Filter) obj).until)
                && Objects.equal(filterTags, ((Filter) obj).filterTags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(from, until, filterTags);
    }

}
