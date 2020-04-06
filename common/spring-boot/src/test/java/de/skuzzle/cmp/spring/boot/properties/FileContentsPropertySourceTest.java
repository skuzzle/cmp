package de.skuzzle.cmp.spring.boot.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import de.skuzzle.cmp.spring.boot.properties.FileContentsPropertySource.FileContentsDefinition;

public class FileContentsPropertySourceTest {

    private static final class SuccessTestCase {
        private final String stringToParse;
        private final FileContentsDefinition expectedResult;

        public SuccessTestCase(String stringToParse, FileContentsDefinition expectedResult) {
            this.stringToParse = stringToParse;
            this.expectedResult = expectedResult;
        }

        DynamicTest toTest() {
            return DynamicTest.dynamicTest(stringToParse, () -> {
                assertThat(FileContentsDefinition.parseValue(stringToParse)).isEqualTo(expectedResult);
            });
        }
    }

    private static final class FailureTestCase {
        private final String stringToParse;
        private final String expectedFailure;

        public FailureTestCase(String stringToParse, String expectedFailure) {
            this.stringToParse = stringToParse;
            this.expectedFailure = expectedFailure;
        }

        DynamicTest toTest() {
            return DynamicTest.dynamicTest(stringToParse, () -> {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> FileContentsDefinition.parseValue(stringToParse))
                        .withMessageContaining(expectedFailure);
            });
        }
    }

    @TestFactory
    Stream<DynamicTest> testParseInvalidFileDefinitions() throws Exception {
        return Arrays.asList(
                new FailureTestCase("fileContents;string;", "Missing path"),
                new FailureTestCase("fileContents;foo;/dev/null", "Type must be either"))
                .stream()
                .map(FailureTestCase::toTest);
    }

    @TestFactory
    Stream<DynamicTest> testParseFileDefinitionsSuccess() throws Exception {
        final String defaultCs = Charset.defaultCharset().name();
        return Arrays.asList(
                new SuccessTestCase("fileContents;/dev/null",
                        new FileContentsDefinition("string", defaultCs, "/dev/null")),
                new SuccessTestCase("fileContents;bytes;/dev/null",
                        new FileContentsDefinition("bytes", null, "/dev/null")),
                new SuccessTestCase("fileContents;string;/dev/null",
                        new FileContentsDefinition("string", defaultCs, "/dev/null")),
                new SuccessTestCase("fileContents;string;UTF-8;/dev/null",
                        new FileContentsDefinition("string", "UTF-8", "/dev/null")))
                .stream()
                .map(SuccessTestCase::toTest);
    }

}
