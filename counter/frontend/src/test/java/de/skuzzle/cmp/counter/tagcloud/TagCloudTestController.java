package de.skuzzle.cmp.counter.tagcloud;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.Tags;
import de.skuzzle.cmp.counter.client.TestResponses;

@Controller
class TagCloudTestController {

    @GetMapping("/tagcloud")
    public ModelAndView renderTagCloud() {
        final RestTallyResponse response = TestResponses.tallySheet()
                .addIncrement("1", "", LocalDateTime.now(), "tag1", "tag2")
                .addIncrement("2", "", LocalDateTime.now(), "tag1", "tag3")
                .toResponse();
        final TagCloud cloud = TagCloud.fromBackendResponse("key", response, Tags.fromString("tag1"));
        return new ModelAndView("tallypage/tagcloud :: tagCloud")
                .addObject("cloud", cloud);
    }
}
