package de.skuzzle.cmp.counter.client;

import java.util.Arrays;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(CmpBackendProperties.class)
public class BackendClientConfiguration {

    private final CmpBackendProperties tallyProperties;
    private final ObjectMapper objectMapper;
    private final ClientId clientId;

    public BackendClientConfiguration(CmpBackendProperties tallyProperties, ObjectMapper objectMapper,
            ClientId clientId) {
        this.tallyProperties = tallyProperties;
        this.objectMapper = objectMapper;
        this.clientId = clientId;
    }

    private ClientIdInterceptor idInterceptor() {
        return new ClientIdInterceptor(clientId);
    }

    private RestTemplate restTemplate(String baseUrl) {
        final var jacksonMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        final var restTemplate = new RestTemplate(Arrays.asList(jacksonMessageConverter));
        final var uriBuilderFactory = new DefaultUriBuilderFactory(baseUrl);
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
        restTemplate.getInterceptors().add(idInterceptor());
        return restTemplate;
    }

    @Bean
    public BackendClient tallyClient() {
        final BackendClient client = new DefaultBackendClient(
                restTemplate(tallyProperties.getUrl()),
                restTemplate(tallyProperties.getHealthUrl()));
        return tallyProperties.useResilienceFeatures()
                ? new ResilientBackendClient(client)
                : client;
    }
}
