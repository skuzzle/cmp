package de.skuzzle.tally.frontend.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class TallyClient {

    private static final Logger logger = LoggerFactory.getLogger(TallyClient.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TallyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public TallyApiResponse createNewTallySheet(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        try {
            final ResponseEntity<TallySheet> response = restTemplate.postForEntity("/{name}", null, TallySheet.class, name);
            return TallyApiResponse.success(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            logger.debug("HTTP error while calling backend 'POST /{}", name, e);
            final ErrorResponse errorResponse = error(e.getResponseBodyAsString());
            return TallyApiResponse.fail(e.getStatusCode(), errorResponse);
        }
    }

    public TallyApiResponse getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");

        try {
            final ResponseEntity<TallySheet> response = restTemplate.getForEntity("/public/{key}", TallySheet.class, publicKey);
            return TallyApiResponse.success(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            logger.debug("HTTP error while calling backend 'GET /public/{}", publicKey, e);
            final ErrorResponse errorResponse = error(e.getResponseBodyAsString());
            return TallyApiResponse.fail(e.getStatusCode(), errorResponse);
        }
    }

    public TallyApiResponse increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        try {
            final ResponseEntity<TallySheet> response = restTemplate.postForEntity("/admin/{key}", increment, TallySheet.class, adminKey);
            return TallyApiResponse.success(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            logger.debug("HTTP error while calling backend 'POST /admin/{}", adminKey, e);
            final ErrorResponse errorResponse = error(e.getResponseBodyAsString());
            return TallyApiResponse.fail(e.getStatusCode(), errorResponse);
        }
    }

    public boolean deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        try {
            restTemplate.delete("/admin/{key}", adminKey);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting tally sheet with key '{}'", adminKey, e);
            return false;
        }
    }

    private ErrorResponse error(String errorResponseBody) {
        try {
            return objectMapper.readValue(errorResponseBody, ErrorResponse.class);
        } catch (IOException e) {
            logger.error("Error while deserializing exception response: {}", errorResponseBody, e);
            return new ErrorResponse(e.getMessage(), e.getClass().getName());
        }
    }

}
