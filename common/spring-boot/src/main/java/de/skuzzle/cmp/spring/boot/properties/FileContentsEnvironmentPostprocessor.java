package de.skuzzle.cmp.spring.boot.properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class FileContentsEnvironmentPostprocessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        FileContentsPropertySource.addToEnvironment(environment);
    }

}
