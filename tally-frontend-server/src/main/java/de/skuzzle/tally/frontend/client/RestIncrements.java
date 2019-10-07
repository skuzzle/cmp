package de.skuzzle.tally.frontend.client;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
}
