package de.skuzzle.cmp.common.ratelimit;

public enum RateLimitedOperations implements RateLimitedOperation {
    FREE(0),
    CHEAP(0.01),
    EXPENSIVE(0.1),
    VERY_EXPENSIVE(0.5);

    private final double costs;

    private RateLimitedOperations(double costs) {
        this.costs = costs;
    }

    @Override
    public int calculatePermits(double rps) {
        return (int) Math.round(rps * costs);
    }
}
