package de.skuzzle.cmp.counter.frontpage;

import static de.skuzzle.cmp.turbolinks.TurboRedirectResultMatchers.turboRedirectToUrl;
import static de.skuzzle.cmp.turbolinks.TurboRedirectResultMatchers.turboRedirectToUrlTemplate;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.cmp.counter.FrontendTestSlice;
import de.skuzzle.cmp.counter.TestUserConfigurer;
import de.skuzzle.cmp.counter.client.BackendClient;
import de.skuzzle.cmp.counter.client.TestResponses;

@FrontendTestSlice
@WebMvcTest(controllers = FrontpageController.class)
public class FrontpageControllerTest {

    @Autowired
    private BackendClient tallyClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUserConfigurer testUser;

    @Test
    void testRenderFrontPageInitial() throws Exception {
        testUser.anonymous();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user", "exampleGraph", "recentlyCreated",
                        "totalTallySheetCount", "socialCard"));
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
        mockMvc.perform(post("/counter/_create?name={name}", newCounterName))
                .andExpect(turboRedirectToUrlTemplate("/counter/{adminKey}", expectedAdminKey));
    }

    @Test
    void testDeleteCounter() throws Exception {
        testUser.anonymous();

        final String adminKeyToDelete = "adminKey";
        mockMvc.perform(get("/counter/{adminKey}?action=delete", adminKeyToDelete))
                .andExpect(turboRedirectToUrl("/"));
        verify(tallyClient).deleteTallySheet("adminKey");
    }
}
