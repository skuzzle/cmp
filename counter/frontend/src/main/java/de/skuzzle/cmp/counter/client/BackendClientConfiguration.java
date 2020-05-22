package de.skuzzle.cmp.counter.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Configuration
@EnableConfigurationProperties(CmpBackendProperties.class)
public class BackendClientConfiguration {

    // Nullable to simplify unit test and migration from RestTemplate
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private final CmpBackendProperties tallyProperties;
    private final ClientId clientId;

    public BackendClientConfiguration(
            @Autowired(required = false) OAuth2AuthorizedClientManager authorizedClientManager,
            CmpBackendProperties tallyProperties,
            ClientId clientId) {
        this.authorizedClientManager = authorizedClientManager;
        this.tallyProperties = tallyProperties;
        this.clientId = clientId;
    }

    @Bean
    public WebClient webClient() {
        final Builder builder = WebClient.builder()
                .baseUrl(tallyProperties.getUrl());
        if (authorizedClientManager == null) {
            return builder.build();
        }
        final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);
        return builder
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

    @Bean
    public BackendClient tallyClient(WebClient webClient) {
        final BackendClient client = new ReactiveBackendClient(webClient);
        return tallyProperties.useResilienceFeatures()
                ? new ResilientBackendClient(client)
                : client;
    }
}
