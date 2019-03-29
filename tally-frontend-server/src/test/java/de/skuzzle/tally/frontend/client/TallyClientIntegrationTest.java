package de.skuzzle.tally.frontend.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = "tally.backend.url=http://localhost:6565")
@AutoConfigureStubRunner(ids = "de.skuzzle.tally:tally-backend:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class TallyClientIntegrationTest {

    @Autowired
    private TallyClient tallyClient;

    @Test
    void testCreateTallySheet() throws Exception {
        final TallySheet sheet = tallyClient.createNewTallySheet("name");
    }
}
