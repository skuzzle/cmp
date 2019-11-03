package de.skuzzle.cmp.frontend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class DetectDebugStatementsTest {

    private Set<Path> getRootFolders() {
        return Set.of(
                Path.of("./src/main/js"),
                Path.of("./src/main/resources/templates"));
    }

    private Set<Pattern> patternsToDetect() {
        return Set.of(
                Pattern.compile("console\\.log\\("),
                Pattern.compile("alert\\("));
    }

    private Set<String> getExtensions() {
        return Set.of(".js", ".html");
    }

    private String getExtension(Path path) {
        final String name = path.getFileName().toString();
        final int i = name.lastIndexOf('.');
        return i < 0 ? name : name.substring(i);
    }

    @Test
    void testDetectDebugStatements() throws Exception {
        final Set<String> extensionToAnalyze = getExtensions();
        final Set<Pattern> patternsToDetect = patternsToDetect();

        getRootFolders().stream()
                .flatMap(root -> find(root, extensionToAnalyze))
                .forEach(file -> assertDoesNotContain(file, patternsToDetect));
    }

    private void assertDoesNotContain(Path origin, Collection<Pattern> patternsToDetect) {
        final String s = read(origin);
        for (final Pattern pattern : patternsToDetect) {
            if (pattern.matcher(s).find()) {
                throw new AssertionFailedError(String.format("%s found in %s", pattern.toString(), origin.toString()));
            }
        }
    }

    private String read(Path path) {
        try {
            return Files.readString(path);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Stream<Path> find(Path root, Set<String> extensionsToAnalyze) {
        try {
            return Files.find(root, Integer.MAX_VALUE,
                    (path, bfa) -> extensionsToAnalyze.contains(getExtension(path)));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
