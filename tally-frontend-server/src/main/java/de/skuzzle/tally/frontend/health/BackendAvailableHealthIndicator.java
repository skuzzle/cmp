package de.skuzzle.tally.frontend.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import de.skuzzle.tally.frontend.client.TallyClient;

@Component
class BackendAvailableHealthIndicator implements HealthIndicator {

    private final TallyClient client;

    public BackendAvailableHealthIndicator(TallyClient client) {
        this.client = client;
    }

    @Override
    public Health health() {
        return client.isHealthy()
                ? Health.up().build()
                : Health.down().build();
    }

}
