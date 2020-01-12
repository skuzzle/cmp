package de.skuzzle.cmp.counter.tagcloud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.cmp.counter.frontend.slice.mvc.FrontendTestSlice;

@FrontendTestSlice
@WebMvcTest(controllers = TagCloudTestController.class)
public class TagCloudRenderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRenderTagCloud() throws Exception {
        mockMvc.perform(get("/tagcloud")).andExpect(status().isOk());
    }
}
