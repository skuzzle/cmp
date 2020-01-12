package de.skuzzle.cmp.counter.timeline;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import de.skuzzle.cmp.counter.frontend.slice.mvc.FrontendTestSlice;

@FrontendTestSlice
@WebMvcTest(controllers = TimelineTestController.class)
public class TimelineRenderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRenderReadOnlyMobile() throws Exception {
        mockMvc.perform(get("/timeline?mobile=true&adminKey="))
                .andExpect(status().isOk());
    }

    @Test
    void testRenderAdminMobile() throws Exception {
        mockMvc.perform(get("/timeline?mobile=true&adminKey=adminKey"))
                .andExpect(status().isOk());
    }

    @Test
    void testRenderReadOnlyNotMobile() throws Exception {
        mockMvc.perform(get("/timeline?mobile=false&adminKey="))
                .andExpect(status().isOk());
    }

    @Test
    void testRenderAdminNotMobile() throws Exception {
        mockMvc.perform(get("/timeline?mobile=false&adminKey=adminKey"))
                .andExpect(status().isOk());
    }
}
