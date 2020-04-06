package de.skuzzle.cmp.spring.boot.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileContentsPropertySourceSpringTest {

    @Value("${testPropertyIso}")
    private String testPropertyIso;

    @Value("${testPropertyUtf8}")
    private String testPropertyUtf8;

    @Value("${testPropertyBytes}")
    private byte[] testPropertyBytes;

    @Test
    void testPropertyIso() throws Exception {
        assertThat(testPropertyIso).isEqualTo("öäü");
    }

    @Test
    void testPropertyUtf8() throws Exception {
        assertThat(testPropertyUtf8).isEqualTo("öäü");
    }

    @Test
    void testPropertyBytes() throws Exception {
        assertThat(testPropertyBytes).isEqualTo("öäü".getBytes(StandardCharsets.ISO_8859_1));
    }
}
