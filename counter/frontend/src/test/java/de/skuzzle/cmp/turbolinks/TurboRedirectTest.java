package de.skuzzle.cmp.turbolinks;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

public class TurboRedirectTest {

    @Controller
    static class TurbolinksTestController {

        @GetMapping("/ok")
        @ResponseBody
        public String ok() {
            return "ok";
        }

        @GetMapping("/redirectGet")
        public View redirectGet() {
            return TurboRedirect.to("/target");
        }

        @GetMapping("/redirectGetWithParameter")
        public View redirectGetWithParameter() {
            return TurboRedirect.to("/target?someParameter=1");
        }

        @PostMapping("/redirectPost")
        public View redirectPost() {
            return TurboRedirect.to("/target", RedirectOptions.defaultOptions().withJavaScriptResponse());
        }
    }

    private final MockMvc mockmvc = MockMvcBuilders
            .standaloneSetup(new TurbolinksTestController())
            .addFilter(new TurbolinksLocationFilter(), "/*")
            .build();

    @Test
    void testSetTurbolinksLocationFromParameter() throws Exception {
        mockmvc.perform(get("/ok?tll=1"))
                .andExpect(MockMvcResultMatchers.header().string("Turbolinks-Location", "http://localhost/ok"));
    }

    @Test
    void testNormalGetRequest() throws Exception {
        mockmvc.perform(get("/redirectGet"))
                .andExpect(redirectedUrl("/target"));
    }

    @Test
    void testTurbolinksGetRequest() throws Exception {
        mockmvc.perform(get("/redirectGet")
                .with(turbolinksReferrer("/xyz")))
                .andExpect(redirectedUrl("/target?tll=1"));
    }

    @Test
    void testTurbolinksGetRequestWithParameterRedirect() throws Exception {
        mockmvc.perform(get("/redirectGetWithParameter")
                .with(turbolinksReferrer("/xyz")))
                .andExpect(redirectedUrl("/target?someParameter=1&tll=1"));
    }

    @Test
    void testNormalPostRequest() throws Exception {
        throw new IllegalStateException("tbd;;;;;");
    }

    @Test
    void testTurbolinksXhrPostRequest() throws Exception {
        mockmvc.perform(post("/redirectPost"))
                .andExpect(content().contentType("text/JavaScript"))
                .andExpect(content().string(Matchers.containsString("Turbolinks.clearCache()")))
                .andExpect(content().string(Matchers.containsString("Turbolinks.visit('/target'")));
    }

    private static RequestPostProcessor turbolinksReferrer(String referrer) {
        return request -> {
            request.addHeader("Turbolinks-Referrer", referrer);
            return request;
        };
    }

}
