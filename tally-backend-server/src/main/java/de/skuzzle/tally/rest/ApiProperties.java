package de.skuzzle.tally.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cmp.api")
class ApiProperties {
    private Ratelimit ratelimit = new Ratelimit();

    public Ratelimit getRatelimit() {
        return this.ratelimit;
    }

    public void setRatelimit(Ratelimit ratelimit) {
        this.ratelimit = ratelimit;
    }

    public static class Ratelimit {
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
