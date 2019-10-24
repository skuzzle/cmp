package de.skuzzle.tally.frontend.tallypage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.tally.frontend.auth.TallyUser;
import de.skuzzle.tally.frontend.client.RestTallyIncrement;
import de.skuzzle.tally.frontend.client.TallyClient;
import de.skuzzle.tally.frontend.client.TestResponses;
import de.skuzzle.tally.frontend.client.TestResponses.TallySheetResponseBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TallyPageControllerTest {

    @MockBean
    private TallyUser currentUser;
    @MockBean
    private TallyClient tallyClient;

    @Autowired
    private MockMvc mockMvc;

    private final TallySheetResponseBuilder tallySheetPublic = TestResponses.tallySheet().withAdminKey(null);
    private final TallySheetResponseBuilder tallySheetAdmin = TestResponses.tallySheet();

    private void withAnonymousUser() {
        when(currentUser.isLoggedIn()).thenReturn(false);
        when(currentUser.getName()).thenReturn("unknown");
    }

    private void withUserNamed(String name) {
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getName()).thenReturn(name);
    }

    @BeforeEach
    void setupCurrentTallySheet() {
        when(tallyClient.getTallySheet(tallySheetPublic.getPublicKey())).thenReturn(this.tallySheetPublic.toResponse());
        when(tallyClient.getTallySheet(tallySheetAdmin.getAdminKey())).thenReturn(this.tallySheetAdmin.toResponse());
    }

    @Test
    void testViewEmptyCounter() throws Exception {
        // should give same result when logged in
        withAnonymousUser();

        mockMvc.perform(get("/{publicKey}", tallySheetPublic.getPublicKey()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tally", "timeline", "increments", "graph", "user"));
    }

    @Test
    void testViewCounterWithEntries() throws Exception {
        // should give same result when anonymous
        withUserNamed("Heini");

        // create some increments spread across multiple monthz
        tallySheetAdmin.addIncrement("1", "first", LocalDateTime.now(), "tag1", "tag2")
                .addIncrement("2", "second", LocalDateTime.now().minus(Period.ofMonths(1)))
                .addIncrement("3", "third", LocalDateTime.now().minus(Period.ofMonths(1)))
                .addIncrement("4", "fourth", LocalDateTime.now().minus(Period.ofMonths(2)), "tag");

        mockMvc.perform(get("/{adminKey}", tallySheetAdmin.getAdminKey()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tally", "timeline", "increments", "graph", "user"));
    }

    @Test
    void testAssignToCurrentUser() throws Exception {
        withUserNamed("Heini");

        final String adminKey = tallySheetAdmin.getAdminKey();
        when(tallyClient.assignToCurrentUser(adminKey)).thenReturn(true);

        mockMvc.perform(get("/{adminKey}?action=assignToCurrentUser", adminKey))
                .andExpect(redirectedUrlTemplate("/{adminKey}", adminKey));
        verify(tallyClient).assignToCurrentUser(adminKey);
    }

    @Test
    void testDeleteIncrement() throws Exception {
        withAnonymousUser();

        final String incrementId = "incrementId";
        final String adminKey = tallySheetAdmin.getAdminKey();
        when(tallyClient.deleteIncrement(adminKey, incrementId)).thenReturn(true);

        mockMvc.perform(get("/{adminKey}/increment/{incrementId}?action=delete", adminKey, incrementId))
                .andExpect(redirectedUrlTemplate("/{adminKey}", adminKey));

        verify(tallyClient).deleteIncrement(adminKey, incrementId);
    }

    @Test
    void testIncrement() throws Exception {
        withAnonymousUser();

        final String adminKey = tallySheetAdmin.getAdminKey();
        final String description = "description";
        final String tags = "comma,separated, leading space,trailing space ,, ";
        final String incrementDateUTC = "1987-12-09";

        mockMvc.perform(post("/{adminKey}?description={description}&tags={tags}&incrementDateUTC={incrementDateUTC}",
                adminKey, description, tags, incrementDateUTC))
                .andExpect(redirectedUrlTemplate("/{adminKey}", adminKey));

        final ArgumentCaptor<RestTallyIncrement> incrementCaptor = ArgumentCaptor.forClass(RestTallyIncrement.class);
        verify(tallyClient).increment(eq(adminKey), incrementCaptor.capture());

        final RestTallyIncrement increment = incrementCaptor.getValue();
        assertThat(increment.getDescription()).isEqualTo(description);
        assertThat(increment.getId()).isNull();
        assertThat(increment.getTags()).containsExactlyInAnyOrder("separated", "comma", "leading space",
                "trailing space");
        assertThat(increment.getIncrementDateUTC())
                .isEqualToIgnoringSeconds(LocalDateTime.of(LocalDate.of(1987, 12, 9), LocalTime.now()));
    }
}
