package de.skuzzle.cmp.counter.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;

class DefaultBackendClient implements BackendClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultBackendClient.class);

    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateHealth;

    public DefaultBackendClient(RestTemplate restTemplate, RestTemplate restTemplateHealth) {
        this.restTemplate = restTemplate;
        this.restTemplateHealth = restTemplateHealth;
    }

    @Override
    public boolean isHealthy() {
        try {
            restTemplateHealth.getForEntity("/actuator/health", Object.class);
            return true;
        } catch (final Exception e) {
            logger.error("Backend seems not be available", e);
            return false;
        }
    }

    @Override
    public RestTallyMetaInfoResponse getMetaInfo() {
        return restTemplate.getForObject("/_meta", RestTallyMetaInfoResponse.class);
    }

    @Override
    public RestTallySheetsReponse listTallySheets() {
        return restTemplate.getForObject("/", RestTallySheetsReponse.class);
    }

    @Override
    public RestTallyResponse createNewTallySheet(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        return restTemplate.postForObject("/{name}", null, RestTallyResponse.class, name);
    }

    @Override
    public RestTallyResponse getTallySheet(String publicKey, Filter filter) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");
        return filter.callBackendUsing(restTemplate, publicKey);
    }

    @Override
    public void increment(String adminKey, RestTallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        restTemplate.postForObject("/{key}/increment", increment, RestTallyResponse.class, adminKey);
    }

    @Override
    public void deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        restTemplate.delete("/{key}", adminKey);
    }

    @Override
    public void deleteIncrement(String adminKey, String incrementId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(incrementId != null, "incrementId must not be null");
        restTemplate.delete("/{key}/increment/{id}", adminKey, incrementId);
    }

    @Override
    public void updateIncrement(String adminKey, RestTallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        restTemplate.put("/{key}/increment", increment, adminKey);
    }

    @Override
    public void assignToCurrentUser(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        restTemplate.postForEntity("/{key}/assignToCurrentUser", null, Object.class, adminKey);
    }

    @Override
    public void changeName(String adminKey, String newName) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(newName != null, "newName must not be null");
        restTemplate.put("/{key}/changeName/{newName}", null, adminKey, newName);
    }

}
