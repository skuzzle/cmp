package de.skuzzle.tally.frontend.client;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;

public class TallyClient {

    private static final Logger logger = LoggerFactory.getLogger(TallyClient.class);

    private final RestTemplate restTemplate;

    public TallyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<TallySheet> createNewTallySheet(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        try {
            return Optional.of(restTemplate.postForObject("/{name}", null, TallySheet.class, name));
        } catch (Exception e) {
            logger.error("Error while creating tally sheet with name '{}'", name, e);
            return Optional.empty();
        }
    }

    public Optional<TallySheet> getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");

        try {
            return Optional.of(restTemplate.getForObject("/public/{key}", TallySheet.class, publicKey));
        } catch (final Exception e) {
            logger.error("Error while retrieving tally sheet with key '{}'", publicKey, e);
            return Optional.empty();
        }
    }

    public Optional<TallySheet> increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        try {
            return Optional.of(restTemplate.postForObject("/admin/{key}", increment, TallySheet.class, adminKey));
        } catch (final Exception e) {
            logger.error("Error while incrementing tally sheet with key '{}'", adminKey, e);
            return Optional.empty();
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

}
