package de.skuzzle.cmp.frontend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "cmp")
public class Version {

    private final String version;

    public Version(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }
}
