package de.skuzzle.cmp.spring.boot.properties;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.google.common.base.Preconditions;

public class FileContentsPropertySource extends PropertySource<FileSystem> {

    private static final Logger log = LoggerFactory.getLogger(FileContentsPropertySource.class);

    public static final String FILE_CONTENT_PROPERTY_SOURCE_NAME = "fileContents;";

    // property format: fileContent;'bytes'|['string'[;<encoding>]];<path
    private static final String PREFIX = "fileContents;";

    public FileContentsPropertySource(String name, FileSystem source) {
        super(name, source);
    }

    public FileContentsPropertySource(String name) {
        super(name, FileSystems.getDefault());
    }

    @Override
    public Object getProperty(String name) {
        if (!name.startsWith(PREFIX)) {
            return null;
        }
        log.trace("Reading property for '{}' from file", name);
        try {
            final FileContentsDefinition contentDefinition = FileContentsDefinition.parseValue(name);
            return contentDefinition.getContents(getSource());
        } catch (final IOException e) {
            throw new IllegalArgumentException("Could not determine file content value from: " + name, e);
        }
    }

    public static void addToEnvironment(ConfigurableEnvironment environment) {
        environment.getPropertySources().addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new FileContentsPropertySource(FILE_CONTENT_PROPERTY_SOURCE_NAME));
        log.trace("FileContentsPropertySource add to Environment");
    }

    static class FileContentsDefinition {

        private final String type;
        private final String encoding;
        private final String path;

        FileContentsDefinition(String type, String encoding, String path) {
            this.type = type;
            this.encoding = encoding;
            this.path = path;
        }

        public static FileContentsDefinition parseValue(String value) {
            Preconditions.checkArgument(value.startsWith(PREFIX));

            final String valueWithoutPrefix = value.substring(PREFIX.length());
            int nextSemicolon = valueWithoutPrefix.indexOf(';');
            if (nextSemicolon >= 0) {
                final String typePart = valueWithoutPrefix.substring(0, nextSemicolon);

                if ("bytes".equals(typePart)) {
                    // fileContents;bytes;<path>
                    final String path = valueWithoutPrefix.substring(nextSemicolon + 1);
                    return new FileContentsDefinition("bytes", null, path);
                } else if ("string".equals(typePart)) {
                    nextSemicolon = valueWithoutPrefix.indexOf(';', nextSemicolon);
                    if (nextSemicolon >= 0) {
                        final int veryNextSemicolon = valueWithoutPrefix.indexOf(';', nextSemicolon + 1);
                        if (veryNextSemicolon >= 0) {
                            // fileContents;string;<encoding>;<path>
                            final String encoding = valueWithoutPrefix.substring(nextSemicolon + 1, veryNextSemicolon);
                            final String path = valueWithoutPrefix.substring(veryNextSemicolon + 1);
                            return new FileContentsDefinition("string", encoding, path);
                        } else {
                            // fileContents;string;<path>
                            final String path = valueWithoutPrefix.substring(nextSemicolon + 1);
                            Preconditions.checkArgument(!path.isEmpty(), "Invalid file definition: %s. Missing path",
                                    value);
                            return new FileContentsDefinition("string", Charset.defaultCharset().name(), path);
                        }
                    } else {
                        // TODO is this even reachable? Add test case
                        // fileContents;string
                        throw new IllegalArgumentException(
                                String.format("Invalid file definition: %s. Missing path", value));
                    }
                } else {
                    throw new IllegalArgumentException(String.format(
                            "Illegal file definition: %s. Type must be either 'bytes' or 'string' but was: '%s'", value,
                            typePart));
                }
            } else {
                // fileContents;<path>
                return new FileContentsDefinition("string", Charset.defaultCharset().name(), valueWithoutPrefix);
            }
        }

        public Object getContents(FileSystem source) throws IOException {
            switch (type) {
            case "string":
                return Files.readString(source.getPath(path), Charset.forName(encoding));
            case "bytes":
                return Files.readAllBytes(source.getPath(path));
            default:
                throw new IllegalStateException("Unknown type: " + type);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, encoding, path);
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof FileContentsDefinition
                    && Objects.equals(type, ((FileContentsDefinition) obj).type)
                    && Objects.equals(path, ((FileContentsDefinition) obj).path)
                    && Objects.equals(encoding, ((FileContentsDefinition) obj).encoding);
        }

        @Override
        public String toString() {
            switch (type) {
            case "string":
                return String.format("%sstring;%s;%s", PREFIX, encoding, path);
            case "bytes":
                return String.format("%sbytes;%s", PREFIX, path);
            }
            throw new IllegalStateException();
        }
    }
}
