package de.skuzzle.cmp.counter.tallypage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.counter.client.BackendClient;
import de.skuzzle.cmp.counter.client.RestIncrements;
import de.skuzzle.cmp.counter.client.RestTallyIncrement;
import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.RestTallySheet;
import de.skuzzle.cmp.counter.client.Tags;
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
    public String redirect(@PathVariable("key") String key) {
        return "redirect:/counter/" + key + "?legacyWarning=true";
    }

    @GetMapping("/counter/{key}")
    public ModelAndView showTallySheet(
            @PathVariable("key") String key,
            @RequestParam(defaultValue = "false") boolean legacyWarning,
            @RequestParam(defaultValue = "") Set<String> tags,
            Device device) {

        final Tags filterTags = Tags.fromCollection(tags);
        final RestTallyResponse response = client.getTallySheet(key, filterTags);

        final RestTallySheet tallySheet = response.getTallySheet();
        final RestIncrements increments = response.getIncrements();

        final Graph graph = Graph.fromHistory(increments.getEntries());
        final Timeline timeline = TimelineBuilder.fromBackendResponse(response);
        final TagCloud tagCloud = TagCloud.fromBackendResponse(key, response, filterTags);

        final boolean mobile = !device.isNormal();
        return new ModelAndView("tallypage/tally", Map.of(
                "filterActive", !filterTags.all().isEmpty(),
                "key", key,
                "tagCloud", tagCloud,
                "tally", tallySheet,
                "timeline", timeline,
                "increments", increments,
                "graph", graph,
                "mobile", mobile,
                "legacyWarning", legacyWarning));
    }

    @PostMapping("/counter/{adminKey}")
    public String incrementTallySheet(
            @PathVariable("adminKey") String adminKey,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,
            @RequestParam("incrementDateUTC") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate incrementDate) {

        final Set<String> tagSet = Tags.fromString(tags).all();
        final RestTallyIncrement increment = RestTallyIncrement.createNew(description,
                LocalDateTime.of(incrementDate, LocalTime.now()), tagSet);

        client.increment(adminKey, increment);
        return "redirect:/counter/" + adminKey;
    }

    @GetMapping(path = "counter/{key}", params = "action=addTag")
    public String addFilterTag(@PathVariable String key) {
        return "redirect:/counter/" + key;
    }

    @GetMapping(path = "/counter/{adminKey}/increment/{incrementId}", params = "action=updateIncrement")
    public String updateIncrement(
            @PathVariable("adminKey") String adminKey,
            @PathVariable("incrementId") String incrementId,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,
            @RequestParam("incrementDateUTC") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate incrementDate) {

        final Set<String> tagSet = Tags.fromString(tags).all();
        final RestTallyIncrement increment = RestTallyIncrement.createWithId(incrementId, description,
                LocalDateTime.of(incrementDate, LocalTime.now()), tagSet);
        client.updateIncrement(adminKey, increment);
        return "redirect:/counter/" + adminKey;
    }

    @GetMapping(path = "/counter/{key}", params = "action=assignToCurrentUser")
    public String assignToCurrentUser(@PathVariable String key) {
        Preconditions.checkState(this.currentUser.isLoggedIn(), "Can't assign to current user: user not logged in");
        client.assignToCurrentUser(key);
        return "redirect:/counter/" + key;
    }

    @GetMapping(path = "/counter/{key}/increment/{incrementId}", params = "action=delete")
    public String deleteIncrement(@PathVariable String key, @PathVariable String incrementId) {
        client.deleteIncrement(key, incrementId);
        return "redirect:/counter/" + key;
    }

    @GetMapping(path = "/counter/{adminKey}", params = { "action=changeName", "newName" })
    public String changeTitle(@PathVariable String adminKey, @RequestParam String newName) {
        client.changeName(adminKey, newName);
        return "redirect:/counter/" + adminKey;
    }
}
