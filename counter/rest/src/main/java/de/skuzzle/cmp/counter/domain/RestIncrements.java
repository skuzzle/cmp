package de.skuzzle.cmp.counter.domain;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

public class RestIncrements {

    private final List<RestTallyIncrement> entries;
    private final int start;
    private final int total;

    private RestIncrements(List<RestTallyIncrement> entries, int start, int total) {
        this.entries = entries;
        this.start = start;
        this.total = total;
    }

    public static RestIncrements empty(int total) {
        return new RestIncrements(Collections.emptyList(), 0, total);
    }

    public static RestIncrements of(IncrementQueryResult result) {
        Preconditions.checkArgument(result != null, "result must not be null");
        return new RestIncrements(RestTallyIncrement.fromDomainObjects(result.getIncrements()),
                result.getStart(),
                result.getTotal());
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
}
