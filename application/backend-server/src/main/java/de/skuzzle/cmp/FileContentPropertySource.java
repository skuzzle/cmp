package de.skuzzle.cmp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class FileContentPropertySource extends PropertySource<FileSystem> {

    private static final Logger log = LoggerFactory.getLogger(FileContentPropertySource.class);

    public static final String FILE_CONTENT_PROPERTY_SOURCE_NAME = "fileContent";

    private static final String PREFIX = "fileContent.";

    public FileContentPropertySource(String name, FileSystem source) {
        super(name, source);
    }

    public FileContentPropertySource(String name) {
        super(name, FileSystems.getDefault());
    }

    @Override
    public Object getProperty(String name) {
        if (!name.startsWith(PREFIX)) {
            return null;
        }
        log.trace("Reading property for '{}' from file", name);
        final String pathString = name.substring(PREFIX.length());
        final Path path = getSource().getPath(pathString);
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Could not determine file content value from: " + name, e);
        }
    }

    public static void addToEnvironment(ConfigurableEnvironment environment) {
        environment.getPropertySources().addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new RandomValuePropertySource(FILE_CONTENT_PROPERTY_SOURCE_NAME));
    }
}
