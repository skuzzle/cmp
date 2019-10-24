package de.skuzzle.tally.frontend.frontpage;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.tally.frontend.auth.TallyUser;
import de.skuzzle.tally.frontend.client.TallyClient;
import de.skuzzle.tally.frontend.client.TestResponses;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class FrontpageControllerTest {

    @MockBean
    private TallyClient tallyClient;
    @MockBean
    private TallyUser currentUser;

    @Autowired
    private MockMvc mockMvc;

    private void withAnonymousUser() {
        when(currentUser.isLoggedIn()).thenReturn(false);
        when(currentUser.getName()).thenReturn("unknown");
    }

    private void withUserNamed(String name) {
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getName()).thenReturn(name);
    }

    @Test
    void testRenderFrontPageInitial() throws Exception {
        withAnonymousUser();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user", "exampleGraph", "recentlyCreated"));
    }

    @Test
    void testRenderFrontendLoggedInNoCounters() throws Exception {
        withUserNamed("Heini");

        when(tallyClient.listTallySheets()).thenReturn(TestResponses.emptyTallySheets().toResponse());
        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("recentlyCreated"));
    }

    @Test
    void testRenderFrontendLoggedInWithCounters() throws Exception {
        withUserNamed("Heini");

        when(tallyClient.listTallySheets()).thenReturn(TestResponses.emptyTallySheets()
                .addTallySheet(TestResponses.tallySheet())
                .addTallySheet(TestResponses.tallySheet())
                .toResponse());
        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("recentlyCreated"));
    }

    @Test
    void testCreateCounter() throws Exception {
        withAnonymousUser();

        final String newCounterName = "MyCounter";
        final String expectedAdminKey = "adminKey";

        when(tallyClient.createNewTallySheet(newCounterName)).thenReturn(TestResponses.tallySheet()
                .withName(newCounterName)
                .withAdminKey(expectedAdminKey)
                .toResponse());
        mockMvc.perform(post("/_create?name={name}", newCounterName))
                .andExpect(redirectedUrlTemplate("/{adminKey}", expectedAdminKey));
    }

    @Test
    void testDeleteCounter() throws Exception {
        withAnonymousUser();

        final String adminKeyToDelete = "adminKey";
        when(tallyClient.deleteTallySheet(adminKeyToDelete)).thenReturn(true);
        mockMvc.perform(get("/{adminKey}?action=delete", adminKeyToDelete))
                .andExpect(redirectedUrl("/"));
        verify(tallyClient).deleteTallySheet("adminKey");
    }
}
