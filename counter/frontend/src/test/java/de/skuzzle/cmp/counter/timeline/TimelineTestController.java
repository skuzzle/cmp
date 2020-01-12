package de.skuzzle.cmp.counter.timeline;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.TestResponses;
import de.skuzzle.cmp.counter.client.TestResponses.TallySheetResponseBuilder;
import de.skuzzle.cmp.counter.timeline.Timeline;
import de.skuzzle.cmp.counter.timeline.TimelineBuilder;

@Controller
public class TimelineTestController {

    @GetMapping("/timeline")
    public ModelAndView renderTimelineReadOnly(
            @RequestParam("mobile") boolean mobile,
            @RequestParam("adminKey") String adminKey) {

        final String nullableAdminKey = adminKey == null || adminKey.isEmpty()
                ? null
                : adminKey;
        final RestTallyResponse response = buildResponse().withAdminKey(nullableAdminKey).toResponse();
        final Timeline timeline = TimelineBuilder.fromBackendResponse(response);
        return new ModelAndView("tallypage/timeline :: timeline")
                .addObject("timeline", timeline)
                .addObject("isMobile", mobile);
    }

    private TallySheetResponseBuilder buildResponse() {
        final LocalDateTime someDay = LocalDate.of(1987, 12, 9).atStartOfDay();
        final LocalDateTime otherDay = LocalDate.of(2019, 6, 3).atStartOfDay();
        return TestResponses.tallySheet()
                .addIncrement("1", "1", someDay)
                .addIncrement("2", "2", someDay)
                .addIncrement("3", "3", otherDay);
    }
}
