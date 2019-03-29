package de.skuzzle.tally.frontend.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class TallyClientConfiguration {

    private final TallyProperties tallyProperties;

    public TallyClientConfiguration(TallyProperties tallyProperties) {
        this.tallyProperties = tallyProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        final var restTemplate = new RestTemplate();
        final var uriBuilderFactory = new DefaultUriBuilderFactory(tallyProperties.getUrl());
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
        return restTemplate;
    }

    @Bean
    public TallyClient tallyClient() {
        return new TallyClient(restTemplate());
    }
}
