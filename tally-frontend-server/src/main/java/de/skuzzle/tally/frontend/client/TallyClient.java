package de.skuzzle.tally.frontend.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

public class TallyClient {

    private static final Logger logger = LoggerFactory.getLogger(TallyClient.class);

    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateHealth;
    private final ObjectMapper objectMapper;

    public TallyClient(RestTemplate restTemplate, RestTemplate restTemplateHealth, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.restTemplateHealth = restTemplateHealth;
        this.objectMapper = objectMapper;
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

    public TallyResult createNewTallySheet(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        try {
            final ResponseEntity<TallyApiResponse> response = restTemplate.postForEntity("/{name}", null,
                    TallyApiResponse.class, name);
            return TallyResult.success(response.getStatusCode(), response.getBody().getTallySheet());
        } catch (final HttpStatusCodeException e) {
            logger.debug("HTTP error while calling backend 'POST /{}", name, e);
            final TallyApiResponse response = error(e.getResponseBodyAsString());
            return TallyResult.fail(e.getStatusCode(), response.getError());
        }
    }

    public TallyResult getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");

        try {
            final ResponseEntity<TallyApiResponse> response = restTemplate.getForEntity("/{key}",
                    TallyApiResponse.class, publicKey);
            return TallyResult.success(response.getStatusCode(), response.getBody().getTallySheet());
        } catch (final HttpStatusCodeException e) {
            logger.debug("HTTP error while calling backend 'GET /{}", publicKey, e);
            final TallyApiResponse response = error(e.getResponseBodyAsString());
            return TallyResult.fail(e.getStatusCode(), response.getError());
        }
    }

    public TallyResult increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        try {
            final ResponseEntity<TallyApiResponse> response = restTemplate.postForEntity("/{key}/increment", increment,
                    TallyApiResponse.class, adminKey);
            return TallyResult.success(response.getStatusCode(), response.getBody().getTallySheet());
        } catch (final HttpStatusCodeException e) {
            logger.debug("HTTP error while calling backend 'POST /{}/increment", adminKey, e);
            final TallyApiResponse response = error(e.getResponseBodyAsString());
            return TallyResult.fail(e.getStatusCode(), response.getError());
        }
    }

    public boolean deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        try {
            restTemplate.delete("/{key}", adminKey);
            return true;
        } catch (final Exception e) {
            logger.error("Error deleting tally sheet with key '{}'", adminKey, e);
            return false;
        }
    }

    public boolean deleteIncrement(String adminKey, String incrementId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(incrementId != null, "incrementId must not be null");
        try {
            restTemplate.delete("/{key}/increment/{id}", adminKey, incrementId);
            return true;
        } catch (final Exception e) {
            logger.error("Error deleting increment {} from sheet with key '{}'", incrementId, adminKey, e);
            return false;
        }
    }

    private TallyApiResponse error(String errorResponseBody) {
        try {
            return objectMapper.readValue(errorResponseBody, TallyApiResponse.class);
        } catch (final IOException e) {
            logger.error("Error while deserializing exception response: {}", errorResponseBody, e);
            return new TallyApiResponse(null, new ErrorResponse(e.getMessage(), e.getClass().getName()));
        }
    }

}
