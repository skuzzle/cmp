package de.skuzzle.tally.frontend.client;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TallyClientConfiguration {

    private final TallyProperties tallyProperties;
    private final ObjectMapper objectMapper;

    public TallyClientConfiguration(TallyProperties tallyProperties, ObjectMapper objectMapper) {
        this.tallyProperties = tallyProperties;
        this.objectMapper = objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        final var jacksonMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        final var restTemplate = new RestTemplate(Arrays.asList(jacksonMessageConverter));
        final var uriBuilderFactory = new DefaultUriBuilderFactory(tallyProperties.getUrl());
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
        return restTemplate;
    }

    @Bean
    public TallyClient tallyClient() {
        return new TallyClient(restTemplate(), objectMapper);
    }
}
