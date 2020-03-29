package de.skuzzle.cmp.common.ratelimit;

import java.util.Objects;

import com.google.common.base.Preconditions;

public interface RateLimitedOperation {

    public static RateLimitedOperation custom(String name, int costs) {
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(costs >= 0, "costs must be >= 0");
        return new RateLimitedOperation() {

            @Override
            public int costs() {
                return costs;
            }

            @Override
            public int hashCode() {
                return Objects.hash(costs);
            }

            @Override
            public boolean equals(Object obj) {
                return obj == this || obj instanceof RateLimitedOperation
                        && costs() == ((RateLimitedOperation) obj).costs();
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    int costs();
}
