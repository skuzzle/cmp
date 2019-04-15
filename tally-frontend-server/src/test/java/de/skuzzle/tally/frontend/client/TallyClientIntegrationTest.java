package de.skuzzle.tally.frontend.client;

import org.assertj.core.api.LocalDateTimeAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = "tally.backend.url=http://localhost:6565")
@AutoConfigureStubRunner(ids = "de.skuzzle.tally:tally-backend:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class TallyClientIntegrationTest {

    @Autowired
    private TallyClient tallyClient;

    @Test
    void testCreateTallySheet() {
        final var response = tallyClient.createNewTallySheet("name").orElseThrow(AssertionError::new);
        assertThat(response.getCreateDateUTC()).isEqualTo(LocalDateTime.of(1987,9,12,11,11,0));
        assertThat(response.getLastModifiedDateUTC()).isEqualTo(LocalDateTime.of(1987,9,12,11,11,0));
    }

    @Test
    void incrementTallySheet() {
        final var increment = new TallyIncrement();
        increment.setDescription("Description");
        increment.setTags(Set.of("tag1", "tag2"));

        final var response = tallyClient.increment("adminKey", increment).orElseThrow(AssertionError::new);
        assertThat(response.getCreateDateUTC()).isEqualTo(LocalDateTime.of(1987,9,12,11,11,0));
        assertThat(response.getLastModifiedDateUTC()).isEqualTo(LocalDateTime.of(1987,9,12,11,11,0));
    }

    @Test
    void testGetUnknownTallySheet() {
        final var response = tallyClient.getTallySheet("unknownPublicKey");
        assertThat(response).isEmpty();
    }

    @Test
    void testGetExistingTallySheet() {
        final var response = tallyClient.getTallySheet("publicKey");
        assertThat(response).isNotEmpty();
    }

    @Test
    void testDeleteUnknownTallySheet() {
        final var success = tallyClient.deleteTallySheet("unknownAdminKey");
        assertThat(success).isFalse();
    }

    @Test
    void testDeleteTallySheet() {
        final var success = tallyClient.deleteTallySheet("adminKey");
        assertThat(success).isTrue();
    }

}
