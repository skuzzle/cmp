package de.skuzzle.tally.frontend.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

class ResilientBackendClient implements BackendClient {

    private final BackendClient delegate;

    ResilientBackendClient(BackendClient delegate) {
        this.delegate = delegate;
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public boolean isHealthy() {
        return delegate.isHealthy();
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public RestTallyMetaInfoResponse getMetaInfo() {
        return delegate.getMetaInfo();
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public RestTallySheetsReponse listTallySheets() {
        return delegate.listTallySheets();
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public RestTallyResponse createNewTallySheet(String name) {
        return delegate.createNewTallySheet(name);
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public RestTallyResponse getTallySheet(String publicKey) {
        return delegate.getTallySheet(publicKey);
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public void increment(String adminKey, RestTallyIncrement increment) {
        delegate.increment(adminKey, increment);
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public void deleteTallySheet(String adminKey) {
        delegate.deleteTallySheet(adminKey);
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public void deleteIncrement(String adminKey, String incrementId) {
        delegate.deleteIncrement(adminKey, incrementId);
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public void assignToCurrentUser(String adminKey) {
        delegate.assignToCurrentUser(adminKey);
    }

    @Override
    @CircuitBreaker(name = "backendClient")
    @Retry(name = "backendClient")
    public void changeName(String adminKey, String newTitle) {
        delegate.changeName(adminKey, newTitle);
    }
}
