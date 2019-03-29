package de.skuzzle.tally.frontend.client;

import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;

public class TallyClient {

    private final RestTemplate restTemplate;

    public TallyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TallySheet createNewTallySheet(String name) {
        Preconditions.checkArgument(name != null, "name must not be null");
        return restTemplate.postForObject("/{name}", null, TallySheet.class, name);
    }

    public TallySheet getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");
        return restTemplate.getForObject("/public/{key}", TallySheet.class, publicKey);
    }

    public TallySheet increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(increment != null, "increment must not be null");
        return restTemplate.postForObject("/admin/{key}", increment, TallySheet.class, adminKey);
    }

}
