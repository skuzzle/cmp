package de.skuzzle.tally.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest
public class TallyBase {

    @Autowired
    private WebApplicationContext webAppCtx;
    @Autowired
    private TallyService tallyService;

    private TallySheet existingTallySheet;

    @BeforeEach
    public void setup() {
        this.existingTallySheet = tallyService.createNewTallySheet("existing");
        RestAssuredMockMvc.webAppContextSetup(webAppCtx);
    }

    public String existingTallySheetPublicUri() {
        return "/public/" + existingTallySheet.getPublicKey();
    }

    public String existingTallySheetAdminUri() {
        return "/public/" + existingTallySheet.getAdminKey();
    }
}
