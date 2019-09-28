package de.skuzzle.tally.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tally.api")
class ApiProperties {
    private double requestsPerMinute;

    public double getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public void setRequestsPerMinute(double requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }
}
