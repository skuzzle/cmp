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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.tally.frontend.auth.TestUserConfigurer;
import de.skuzzle.tally.frontend.client.TallyClient;
import de.skuzzle.tally.frontend.client.TestResponses;
import de.skuzzle.tally.frontend.slice.mvc.FrontendTestSlice;

@FrontendTestSlice
@WebMvcTest(controllers = FrontpageController.class)
public class FrontpageControllerTest {

    @Autowired
    private TallyClient tallyClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUserConfigurer testUser;

    @Test
    void testRenderFrontPageInitial() throws Exception {
        testUser.anonymous();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user", "exampleGraph", "recentlyCreated"));
    }

    @Test
    void testRenderFrontendLoggedInNoCounters() throws Exception {
        testUser.authenticatedWithName("Heini");

        when(tallyClient.listTallySheets()).thenReturn(TestResponses.emptyTallySheets().toResponse());
        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("recentlyCreated"));
    }

    @Test
    void testRenderFrontendLoggedInWithCounters() throws Exception {
        testUser.authenticatedWithName("Heini");

        when(tallyClient.listTallySheets()).thenReturn(TestResponses.emptyTallySheets()
                .addTallySheet(TestResponses.tallySheet())
                .addTallySheet(TestResponses.tallySheet())
                .toResponse());
        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("recentlyCreated"));
    }

    @Test
    void testCreateCounter() throws Exception {
        testUser.anonymous();

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
        testUser.anonymous();

        final String adminKeyToDelete = "adminKey";
        when(tallyClient.deleteTallySheet(adminKeyToDelete)).thenReturn(true);
        mockMvc.perform(get("/{adminKey}?action=delete", adminKeyToDelete))
                .andExpect(redirectedUrl("/"));
        verify(tallyClient).deleteTallySheet("adminKey");
    }
}
