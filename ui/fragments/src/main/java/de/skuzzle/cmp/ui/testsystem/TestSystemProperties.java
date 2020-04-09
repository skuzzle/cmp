package de.skuzzle.cmp.ui.testsystem;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cmp.ui")
public class TestSystemProperties {

    private boolean testSystemWarning = false;

    public void setTestSystemWarning(boolean testSystemWarning) {
        this.testSystemWarning = testSystemWarning;
    }

    public boolean isTestSystemWarning() {
        return this.testSystemWarning;
    }
}
