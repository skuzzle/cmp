package de.skuzzle.tally.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tally.api")
class ApiProperties {
    private RateLimit rateLimit = new RateLimit();

    public RateLimit getRateLimit() {
        return this.rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public static class RateLimit {
        private double rps = 10.0;
        private boolean enabled = true;

        public double getRps() {
            return this.rps;
        }

        public void setRps(double rps) {
            this.rps = rps;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

    }
}
