package de.skuzzle.tally.frontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;

public class TallyClient {

    private static final Logger logger = LoggerFactory.getLogger(TallyClient.class);

    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateHealth;

    public TallyClient(RestTemplate restTemplate, RestTemplate restTemplateHealth) {
        this.restTemplate = restTemplate;
        this.restTemplateHealth = restTemplateHealth;
    }

    public boolean isHealthy() {
        try {
            restTemplateHealth.getForEntity("/actuator/health", Object.class);
            return true;
        } catch (final Exception e) {
            logger.error("Backend seems not be available", e);
            return false;
        }
    }

    public RestTallySheetsReponse listTallySheets() {
        return restTemplate.getForObject("/", RestTallySheetsReponse.class);
    }

    public RestTallyResponse createNewTallySheet(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        return restTemplate.postForObject("/{name}", null, RestTallyResponse.class, name);
    }

    public RestTallyResponse getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");
        return restTemplate.getForObject("/{key}", RestTallyResponse.class, publicKey);
    }

    public void increment(String adminKey, RestTallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        restTemplate.postForObject("/{key}/increment", increment, RestTallyResponse.class, adminKey);
    }

    public void deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        restTemplate.delete("/{key}", adminKey);
    }

    public void deleteIncrement(String adminKey, String incrementId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(incrementId != null, "incrementId must not be null");
        restTemplate.delete("/{key}/increment/{id}", adminKey, incrementId);
    }

    public void assignToCurrentUser(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        restTemplate.postForEntity("/{key}/assignToCurrentUser", null, Object.class, adminKey);
    }

}
