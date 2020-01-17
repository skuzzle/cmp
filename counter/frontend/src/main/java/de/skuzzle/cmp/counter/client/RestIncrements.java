package de.skuzzle.cmp.counter.client;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class RestIncrements {

    private final List<RestTallyIncrement> entries;
    private final int start;
    private final int total;

    RestIncrements(List<RestTallyIncrement> entries, int start, int total) {
        this.entries = entries.stream()
                .sorted(Comparator.comparing(RestTallyIncrement::getIncrementDateUTC).reversed())
                .collect(Collectors.toList());
        this.start = start;
        this.total = total;
    }

    public int getStart() {
        return this.start;
    }

    public int getTotal() {
        return this.total;
    }

    public List<RestTallyIncrement> getEntries() {
        return this.entries;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public RestTallyIncrement getIncrement(String incrementId) {
        Preconditions.checkArgument(incrementId != null, "incrementId must not be null");
        return entries.stream()
                .filter(increment -> increment.getId().equals(incrementId))
                .findFirst()
                .orElseThrow();
    }
}
