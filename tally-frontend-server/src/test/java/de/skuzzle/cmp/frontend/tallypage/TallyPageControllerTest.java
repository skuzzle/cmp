package de.skuzzle.cmp.frontend.tallypage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.cmp.frontend.client.RestTallyIncrement;
import de.skuzzle.cmp.frontend.tallypage.TallyPageController;
import de.skuzzle.tally.frontend.auth.TestUserConfigurer;
import de.skuzzle.tally.frontend.client.TestTallyClientConfigurer;
import de.skuzzle.tally.frontend.slice.mvc.FrontendTestSlice;

@FrontendTestSlice
@WebMvcTest(controllers = TallyPageController.class)
public class TallyPageControllerTest {

    @Autowired
    private TestUserConfigurer testUser;
    @Autowired
    private TestTallyClientConfigurer clientConfigurer;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testViewEmptyCounter() throws Exception {
        // should give same result when logged in
        testUser.anonymous();

        mockMvc.perform(get("/{publicKey}", clientConfigurer.getPublicKey()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tally", "timeline", "increments", "graph", "user"));
    }

    @Test
    void testViewCounterWithEntries() throws Exception {
        // should give same result when anonymous
        testUser.authenticatedWithName("Heini");

        // create some increments spread across multiple monthz
        clientConfigurer.configureAdminReply(
                tallySheet -> tallySheet
                        .addIncrement("1", "first", LocalDateTime.now(), "tag1", "tag2")
                        .addIncrement("2", "second", LocalDateTime.now().minus(Period.ofMonths(1)))
                        .addIncrement("3", "third", LocalDateTime.now().minus(Period.ofMonths(1)))
                        .addIncrement("4", "fourth", LocalDateTime.now().minus(Period.ofMonths(2)), "tag"));

        mockMvc.perform(get("/{adminKey}", clientConfigurer.getAdminKey()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tally", "timeline", "increments", "graph", "user"));
    }

    @Test
    void testAssignToCurrentUser() throws Exception {
        testUser.authenticatedWithName("Heini");

        final String adminKey = clientConfigurer.getAdminKey();

        mockMvc.perform(get("/{adminKey}?action=assignToCurrentUser", adminKey))
                .andExpect(redirectedUrlTemplate("/{adminKey}", adminKey));
        clientConfigurer.verify().assignToCurrentUser(adminKey);
    }

    @Test
    void testDeleteIncrement() throws Exception {
        testUser.anonymous();

        final String incrementId = "incrementId";
        final String adminKey = clientConfigurer.getAdminKey();

        mockMvc.perform(get("/{adminKey}/increment/{incrementId}?action=delete", adminKey, incrementId))
                .andExpect(redirectedUrlTemplate("/{adminKey}", adminKey));

        clientConfigurer.verify().deleteIncrement(adminKey, incrementId);
    }

    @Test
    void testIncrement() throws Exception {
        testUser.anonymous();

        final String adminKey = clientConfigurer.getAdminKey();
        final String description = "description";
        final String tags = "comma,separated, leading space,trailing space ,, ";
        final String incrementDateUTC = "1987-12-09";

        mockMvc.perform(post("/{adminKey}?description={description}&tags={tags}&incrementDateUTC={incrementDateUTC}",
                adminKey, description, tags, incrementDateUTC))
                .andExpect(redirectedUrlTemplate("/{adminKey}", adminKey));

        final ArgumentCaptor<RestTallyIncrement> incrementCaptor = ArgumentCaptor.forClass(RestTallyIncrement.class);
        clientConfigurer.verify().increment(eq(adminKey), incrementCaptor.capture());

        final RestTallyIncrement increment = incrementCaptor.getValue();
        assertThat(increment.getDescription()).isEqualTo(description);
        assertThat(increment.getId()).isNull();
        assertThat(increment.getTags()).containsExactlyInAnyOrder("separated", "comma", "leading space",
                "trailing space");
        assertThat(increment.getIncrementDateUTC())
                .isEqualToIgnoringSeconds(LocalDateTime.of(LocalDate.of(1987, 12, 9), LocalTime.now()));
    }
}
