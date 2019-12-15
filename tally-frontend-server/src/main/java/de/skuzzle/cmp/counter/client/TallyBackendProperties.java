package de.skuzzle.cmp.counter.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "cmp.backend")
class TallyBackendProperties {

    private final String url;
    private final String healthUrl;
    private final boolean useResilienceFeatures;

    public TallyBackendProperties(String url, String healthUrl, boolean useResilienceFeatures) {
        this.url = url;
        this.healthUrl = healthUrl;
        this.useResilienceFeatures = useResilienceFeatures;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHealthUrl() {
        return this.healthUrl;
    }

    public boolean useResilienceFeatures() {
        return this.useResilienceFeatures;
    }
}
