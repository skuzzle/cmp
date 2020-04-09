package de.skuzzle.cmp.ui.testsystem;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
class TestSystemWarningModelAttributes {

    private final TestSystemProperties testSystemProperties;

    public TestSystemWarningModelAttributes(TestSystemProperties testSystemProperties) {
        this.testSystemProperties = testSystemProperties;
    }

    @ModelAttribute("showTestSystemWarning")
    public boolean isTestSystemWarning() {
        return testSystemProperties.isTestSystemWarning();
    }

}
