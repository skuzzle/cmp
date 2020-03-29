package de.skuzzle.cmp.common.ratelimit;

public enum RateLimitedOperations implements RateLimitedOperation {
    FREE(0),
    CHEAP(1),
    EXPENSIVE(10),
    VERY_EXPENSIVE(100);

    private final int costs;

    private RateLimitedOperations(int costs) {
        this.costs = costs;
    }

    @Override
    public int costs() {
        return costs;
    }
}
