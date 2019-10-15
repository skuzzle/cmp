package de.skuzzle.tally.frontend.tallypage;

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

import com.google.common.collect.ImmutableMap;

import de.skuzzle.tally.frontend.auth.TallyUser;
import de.skuzzle.tally.frontend.client.RestIncrements;
import de.skuzzle.tally.frontend.client.RestTallyIncrement;
import de.skuzzle.tally.frontend.client.RestTallyResponse;
import de.skuzzle.tally.frontend.client.RestTallySheet;
import de.skuzzle.tally.frontend.client.TallyClient;
import de.skuzzle.tally.frontend.graphs.Graph;

@Controller
public class TallyPageController {

    private final TallyClient client;

    public TallyPageController(TallyClient client) {
        this.client = client;
    }

    @ModelAttribute("user")
    public TallyUser getUser() {
        return TallyUser.fromCurrentRequestContext();
    }

    @PostMapping("/{adminKey}")
    public String incrementTallySheet(@PathVariable("adminKey") String adminKey,
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

    @GetMapping("/{key}")
    public ModelAndView showTallySheet(@PathVariable("key") String key) {
        final RestTallyResponse response = client.getTallySheet(key).payload().orElseThrow();

        final RestTallySheet tallySheet = response.getTallySheet();
        final RestIncrements increments = response.getIncrements();

        final Graph graph = Graph.fromHistory(increments.getEntries());
        return new ModelAndView("tallypage/tally", ImmutableMap.of(
                "tally", tallySheet,
                "increments", increments,
                "graph", graph));
    }

    @GetMapping(path = "/{key}", params = "action=assignToCurrentUser")
    public String assignToCurrentUser(@PathVariable String key) {
        client.assignToCurrentUser(key);
        return "redirect:/" + key;
    }

    @GetMapping(path = "/{key}/increment/{incrementId}", params = "action=delete")
    public String deleteIncrement(@PathVariable String key, @PathVariable String incrementId) {
        client.deleteIncrement(key, incrementId);
        return "redirect:/" + key;
    }

}
