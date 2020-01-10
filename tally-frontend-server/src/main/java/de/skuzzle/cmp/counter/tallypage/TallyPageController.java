package de.skuzzle.cmp.counter.tallypage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.counter.client.BackendClient;
import de.skuzzle.cmp.counter.client.RestIncrements;
import de.skuzzle.cmp.counter.client.RestTallyIncrement;
import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.RestTallySheet;
import de.skuzzle.cmp.counter.graphs.Graph;
import de.skuzzle.cmp.counter.tagcloud.TagCloud;
import de.skuzzle.cmp.counter.timeline.Timeline;
import de.skuzzle.cmp.counter.timeline.TimelineBuilder;

@Controller
public class TallyPageController {

    private final BackendClient client;
    private final TallyUser currentUser;

    public TallyPageController(BackendClient client, TallyUser currentUser) {
        this.client = client;
        this.currentUser = currentUser;
    }

    @ModelAttribute("user")
    public TallyUser getUser() {
        return currentUser;
    }

    @GetMapping("/{key}")
    public ModelAndView showTallySheet(@PathVariable("key") String key) {
        final RestTallyResponse response = client.getTallySheet(key);

        final RestTallySheet tallySheet = response.getTallySheet();
        final RestIncrements increments = response.getIncrements();

        final Graph graph = Graph.fromHistory(increments.getEntries());
        final Timeline timeline = TimelineBuilder.fromBackendResponse(response);
        final TagCloud tagCloud = TagCloud.fromBackendResponse(response);

        return new ModelAndView("tallypage/tally", ImmutableMap.of(
                "tagCloud", tagCloud,
                "tally", tallySheet,
                "timeline", timeline,
                "increments", increments,
                "graph", graph));
    }

    @PostMapping("/{adminKey}")
    public String incrementTallySheet(
            @PathVariable("adminKey") String adminKey,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,
            @RequestParam("incrementDateUTC") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate incrementDate) {

        final Set<String> tagSet = Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());
        final RestTallyIncrement increment = RestTallyIncrement.createNew(description,
                LocalDateTime.of(incrementDate, LocalTime.now()), tagSet);

        client.increment(adminKey, increment);
        return "redirect:/" + adminKey;
    }

    @GetMapping(path = "/{key}", params = "action=assignToCurrentUser")
    public String assignToCurrentUser(@PathVariable String key) {
        Preconditions.checkState(this.currentUser.isLoggedIn(), "Can't assign to current user: user not logged in");
        client.assignToCurrentUser(key);
        return "redirect:/" + key;
    }

    @GetMapping(path = "/{key}/increment/{incrementId}", params = "action=delete")
    public String deleteIncrement(@PathVariable String key, @PathVariable String incrementId) {
        client.deleteIncrement(key, incrementId);
        return "redirect:/" + key;
    }

    @GetMapping(path = "{key}", params = { "action=changeName", "newName" })
    public String changeTitle(@PathVariable String key, @RequestParam String newName) {
        client.changeName(key, newName);
        return "redirect:/" + key;
    }
}
