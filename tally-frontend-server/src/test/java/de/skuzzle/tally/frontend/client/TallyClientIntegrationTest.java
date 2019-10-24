package de.skuzzle.tally.frontend.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, properties = "tally.backend.url=http://localhost:6565")
@AutoConfigureStubRunner(ids = "de.skuzzle.tally:tally-backend:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class TallyClientIntegrationTest {

    @Autowired
    private TallyClient tallyClient;

    @Test
    void testCreateTallySheet() {
        final var apiResponse = tallyClient.createNewTallySheet("name");
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.CREATED);

        final RestTallySheet tallySheet = apiResponse.payload().orElseThrow().getTallySheet();
        assertThat(tallySheet.getCreateDateUTC()).isEqualTo(LocalDateTime.of(1987, 9, 12, 11, 11, 0, 123000000));
        assertThat(tallySheet.getLastModifiedDateUTC()).isEqualTo(LocalDateTime.of(1987, 9, 12, 11, 11, 0, 123000000));
    }

    @Test
    void incrementTallySheet() {
        final var increment = RestTallyIncrement.createNew("Description",
                LocalDateTime.of(2019, 04, 12, 11, 21, 32, 123000000), Set.of("tag1", "tag2"));

        final boolean result = tallyClient.increment("adminKey1", increment);
        assertThat(result).isTrue();
    }

    @Test
    void testGetUnknownTallySheet() {
        final var apiResponse = tallyClient.getTallySheet("unknownPublicKey");
        assertThat(apiResponse.isError()).isTrue();

        final RestErrorMessage errorResponse = apiResponse.error().orElseThrow();
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("unknownPublicKey");
        assertThat(errorResponse.getOrigin()).isEqualTo("de.skuzzle.tally.service.TallySheetNotAvailableException");
    }

    @Test
    void testGetExistingTallySheet() {
        final var apiResponse = tallyClient.getTallySheet("publicKey1");
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(apiResponse.isSuccess());
    }

    @Test
    void testDeleteUnknownTallySheet() {
        final var success = tallyClient.deleteTallySheet("unknownAdminKey");
        assertThat(success).isFalse();
    }

    @Test
    void testDeleteTallySheet() {
        final var success = tallyClient.deleteTallySheet("adminKey1");
        assertThat(success).isTrue();
    }

    @Test
    void testDeleteIncrement() throws Exception {
        final var success = tallyClient.deleteIncrement("adminKey2", "incrementId");
        assertThat(success).isTrue();
    }

    @Test
    void testDeleteUnknownIncrement() throws Exception {
        final var success = tallyClient.deleteIncrement("adminKey1", "unknownIncrementId");
        assertThat(success).isFalse();
    }

    @Test
    void testAssignToCurrentUser() throws Exception {
        final var success = tallyClient.assignToCurrentUser("adminKey3");
        assertThat(success).isTrue();
    }

    @Test
    void testListTallySheets() throws Exception {
        final var apiResponse = tallyClient.listTallySheets();
        assertThat(apiResponse.payload().orElseThrow().getTallySheets()).hasSize(2);
    }
}
