package de.skuzzle.cmp.counter.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import de.skuzzle.cmp.counter.client.BackendClient;

@Component
class BackendAvailableHealthIndicator implements HealthIndicator {

    private final BackendClient client;

    public BackendAvailableHealthIndicator(BackendClient client) {
        this.client = client;
    }

    @Override
    public Health health() {
        return client.isHealthy()
                ? Health.up().build()
                : Health.down().build();
    }

}
