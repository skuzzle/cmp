package de.skuzzle.cmp.counter.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.counter.TestUserConfigurer;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, properties = {
        "cmp.backend.url=http://localhost:6565",
        "cmp.backend.healthUrl=http://not.used.in.this.test",
        "cmp.version=1" })
@AutoConfigureStubRunner(ids = "de.skuzzle.tally.counter:counter-rest:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class TallyClientIntegrationTest {

    @Autowired
    private BackendClient tallyClient;
    @MockBean
    private TallyUser user;

    @BeforeEach
    void setupUser() {
        new TestUserConfigurer(user).authenticatedWithName("Heini");
    }

    @Test
    void testCreateTallySheet() {
        final var apiResponse = tallyClient.createNewTallySheet("name");

        final RestTallySheet tallySheet = apiResponse.getTallySheet();
        assertThat(tallySheet.getCreateDateUTC()).isEqualTo(LocalDateTime.of(1987, 9, 12, 11, 11, 0, 123000000));
        assertThat(tallySheet.getLastModifiedDateUTC()).isEqualTo(LocalDateTime.of(1987, 9, 12, 11, 11, 0, 123000000));
    }

    @Test
    void incrementTallySheet() {
        final var increment = RestTallyIncrement.createNew("Description",
                LocalDateTime.of(2019, 04, 12, 11, 21, 32, 123000000), Set.of("tag1", "tag2"));

        tallyClient.increment("adminKey1", increment);
    }

    @Test
    void testGetUnknownTallySheet() {
        assertThatExceptionOfType(HttpStatusCodeException.class)
                .isThrownBy(() -> tallyClient.getTallySheet("unknownPublicKey"))
                .matches(e -> e.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetExistingTallySheet() {
        final var apiResponse = tallyClient.getTallySheet("publicKey1");
        assertThat(apiResponse.getTallySheet().getPublicKey()).isEqualTo("publicKey1");
    }

    @Test
    void testDeleteUnknownTallySheet() {
        assertThatExceptionOfType(HttpStatusCodeException.class)
                .isThrownBy(() -> tallyClient.deleteTallySheet("unknownAdminKey"))
                .matches(e -> e.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteTallySheet() {
        tallyClient.deleteTallySheet("adminKey1");
    }

    @Test
    void testDeleteIncrement() throws Exception {
        tallyClient.deleteIncrement("adminKey2", "incrementId");
    }

    @Test
    void testDeleteUnknownIncrement() throws Exception {
        assertThatExceptionOfType(HttpStatusCodeException.class)
                .isThrownBy(() -> tallyClient.deleteIncrement("adminKey1", "unknownIncrementId"))
                .matches(e -> e.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void testAssignToCurrentUser() throws Exception {
        tallyClient.assignToCurrentUser("adminKey3");
    }

    @Test
    void testListTallySheets() throws Exception {
        final var apiResponse = tallyClient.listTallySheets();
        assertThat(apiResponse.getTallySheets()).hasSize(2);
    }

    @Test
    void testGetMetaInformation() throws Exception {
        final var apiResponse = tallyClient.getMetaInfo();
        assertThat(apiResponse.getTotalTallySheetCount()).isEqualTo(3);
    }

    @Test
    void testChangeName() throws Exception {
        tallyClient.changeName("adminKey2", "newName");
    }

    @Test
    void testUpdateIncrement() throws Exception {
        tallyClient.updateIncrement("adminKey2", RestTallyIncrement.createWithId("incrementId",
                "Description", LocalDateTime.now(), Set.of("tag1", "tag2")));
    }
}
