package de.skuzzle.cmp.counter.domain;

import static java.util.Comparator.comparing;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class IncrementQuery {

    private LocalDateTime from = LocalDateTime.MIN;
    private LocalDateTime until = LocalDateTime.MAX;
    private int start = 0;
    private int maxResults = Integer.MAX_VALUE;
    private Set<String> tags = Set.of();

    private IncrementQuery() {
        // hidden
    }

    public static IncrementQuery all() {
        return new IncrementQuery();
    }

    public static IncrementQuery none() {
        return all().maxResults(0);
    }

    public IncrementQuery from(LocalDateTime from) {
        Preconditions.checkArgument(from != null, "until must not be null");
        Preconditions.checkArgument(from.isBefore(until), "value for 'from' (%s) must be before value for 'until' (%s)",
                from, until);
        this.from = from;
        return this;
    }

    public IncrementQuery until(LocalDateTime until) {
        Preconditions.checkArgument(until != null, "until must not be null");
        Preconditions.checkArgument(until.isAfter(from), "value for 'until' (%s) must be after value for 'from' (%s)",
                until, from);
        this.until = until;
        return this;
    }

    public IncrementQuery start(int start) {
        Preconditions.checkArgument(start >= 0, "start must be >= 0");
        this.start = start;
        return this;
    }

    public int start() {
        return start;
    }

    public IncrementQuery maxResults(int maxResults) {
        Preconditions.checkArgument(maxResults >= 0, "maxResults must be >= 0");
        this.maxResults = maxResults;
        return this;
    }

    public IncrementQuery havingAllTags(Set<String> tags) {
        Preconditions.checkArgument(tags != null, "tags must not be null");
        this.tags = tags;
        return this;
    }

    public IncrementQueryResult select(int totalCount, Collection<TallyIncrement> allIncrements) {
        Preconditions.checkArgument(allIncrements != null, "allIncrements must not be null");
        final List<TallyIncrement> increments = allIncrements.stream()
                .filter(increment -> increment.getIncrementDateUTC().isAfter(from))
                .filter(increment -> increment.getIncrementDateUTC().isBefore(until))
                .filter(this::byTags)
                .skip(start)
                .limit(maxResults)
                .sorted(comparing(TallyIncrement::getIncrementDateUTC))
                .collect(Collectors.toList());

        return new IncrementQueryResult(increments, this, totalCount);
    }

    private boolean byTags(TallyIncrement increment) {
        return this.tags.isEmpty() || increment.getTags().containsAll(tags);
    }
}
