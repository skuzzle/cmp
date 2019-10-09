package de.skuzzle.tally.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(properties = "tally.api.requestsPerSecond=1000")
@WithMockUser(username = "user1")
public class TallyBase {

    @Autowired
    private WebApplicationContext webAppCtx;
    @MockBean
    private RandomKeyGenerator randomKeyGenerator;
    @Autowired
    private TallyService tallyService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final TallyIncrement increment = TallyIncrement.newIncrementWithId("incrementId",
            "Increment",
            LocalDateTime.now(),
            Collections.emptySet());

    @BeforeEach
    public void setup() {
        mongoTemplate.dropCollection(TallySheet.class);
        when(randomKeyGenerator.generateAdminKey())
                .thenReturn("adminKey")
                .thenReturn("adminKey2")
                .thenReturn("adminKey3");
        when((randomKeyGenerator.generatePublicKey(anyInt())))
                .thenReturn("publicKey")
                .thenReturn("publicKey2")
                .thenReturn("publicKey3");
        tallyService.createNewTallySheet("user1", "existing");
        tallyService.createNewTallySheet("user1", "existing2");
        tallyService.increment("adminKey2", increment);

        RestAssuredMockMvc.webAppContextSetup(webAppCtx);
    }
}
