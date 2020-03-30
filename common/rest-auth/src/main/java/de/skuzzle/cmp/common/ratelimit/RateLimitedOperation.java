package de.skuzzle.cmp.common.ratelimit;

import com.google.common.base.Preconditions;

public interface RateLimitedOperation {

    public static RateLimitedOperation custom(String name, double costs) {
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(costs >= 0, "costs must be >= 0");
        return new RateLimitedOperation() {

            @Override
            public int calculatePermits(double rps) {
                return (int) Math.round(rps * costs);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    public int calculatePermits(double rps);
}
