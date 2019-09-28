package de.skuzzle.tally.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest
public class TallyBase {

    @Autowired
    private WebApplicationContext webAppCtx;
    @MockBean
    private RandomKeyGenerator randomKeyGenerator;
    @Autowired
    private TallyService tallyService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private TallySheet existingTallySheet;

    @BeforeEach
    public void setup() {
        mongoTemplate.dropCollection(TallySheet.class);
        when(randomKeyGenerator.generateAdminKey()).thenReturn("adminKey");
        when((randomKeyGenerator.generatePublicKey(anyInt()))).thenReturn("publicKey");
        this.existingTallySheet = tallyService.createNewTallySheet("existing");
        RestAssuredMockMvc.webAppContextSetup(webAppCtx);
    }

}
