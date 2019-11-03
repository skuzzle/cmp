package de.skuzzle.cmp.counter;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

public class IncrementQueryResult {

    private final List<TallyIncrement> increments;
    private final IncrementQuery originalQuery;
    private final int total;

    IncrementQueryResult(List<TallyIncrement> increments, IncrementQuery originalQuery, int total) {
        Preconditions.checkArgument(increments != null, "increments must not be null");
        Preconditions.checkArgument(originalQuery != null, "originalQuery must not be null");
        this.increments = increments;
        this.originalQuery = originalQuery;
        this.total = total;
    }

    public List<TallyIncrement> getIncrements() {
        return Collections.unmodifiableList(this.increments);
    }

    public IncrementQuery getOriginalQuery() {
        return this.originalQuery;
    }

    public int getStart() {
        return originalQuery.start();
    }

    public int getTotal() {
        return this.total;
    }
}
