package de.skuzzle.tally.frontend.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "cmp.backend")
class TallyProperties {

    private final String url;
    private final String healthUrl;

    public TallyProperties(String url, String healthUrl) {
        this.url = url;
        this.healthUrl = healthUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHealthUrl() {
        return this.healthUrl;
    }

}
