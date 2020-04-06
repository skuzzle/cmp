package de.skuzzle.cmp.counter.domain;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.WebApplicationContext;

import de.skuzzle.cmp.common.random.RandomKey;
import de.skuzzle.cmp.common.random.RandomKeyStrategy;
import de.skuzzle.cmp.common.ratelimit.ApiRateLimiter;
import de.skuzzle.cmp.counter.domain.TallyBase.UserConfig;
import de.skuzzle.cmp.rest.auth.TallyUser;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest
@Import(UserConfig.class)
public class TallyBase {

    private static final UserId USER1 = UserId.wellKnown("test", "user1");
    private static final UserId USER2 = UserId.unknown("user2");

    private final RandomKeyStrategy randomKeyStrategy = mock(RandomKeyStrategy.class);

    @Autowired
    private WebApplicationContext webAppCtx;
    @Autowired
    private TallyService tallyService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @MockBean
    private ApiRateLimiter<HttpServletRequest> rateLimiter;

    @TestConfiguration
    static class UserConfig {

        @Bean
        @Primary
        public TallyUser testUser() {
            return TallyUser.forTestWithId("user1");
        }
    }

    private final TallyIncrement increment = TallyIncrement.newIncrementWithId("incrementId",
            "Increment",
            LocalDateTime.now(),
            Collections.emptySet());

    @BeforeEach
    public void setup() {
        mongoTemplate.dropCollection(TallySheet.class);
        when(randomKeyStrategy.randomUUID())
                .thenReturn("adminKey1")
                .thenReturn("adminKey2")
                .thenReturn("adminKey3")
                .thenReturn("adminKey4");
        when((randomKeyStrategy.ofLength(anyInt())))
                .thenReturn("shareId1")
                .thenReturn("shareId2");
        RandomKey.replaceGlobalStrategyWith(randomKeyStrategy);

        final TallySheet withKey1 = tallyService.createNewTallySheet(USER1, "existing1");
        tallyService.addShare(withKey1.getAdminKey().orElseThrow(), ShareInformation.builder()
                .showIncrements(true)
                .showIncrementDescription(true)
                .showIncrementTags(true).build());

        tallyService.createNewTallySheet(USER1, "existing2");
        tallyService.createNewTallySheet(USER2, "existing3");
        tallyService.increment("adminKey2", increment);

        RestAssuredMockMvc.webAppContextSetup(webAppCtx);
    }
}
