package de.skuzzle.tally.frontend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ImmutableMap;

import de.skuzzle.tally.frontend.client.TallyClient;
import de.skuzzle.tally.frontend.client.TallyIncrement;
import de.skuzzle.tally.frontend.client.TallyResult;
import de.skuzzle.tally.frontend.client.TallySheet;
import de.skuzzle.tally.frontend.graphs.Graph;

@Controller
public class TallyController {

    private final TallyClient client;

    public TallyController(TallyClient client) {
        this.client = client;
    }

    @PostMapping("/_create")
    public String createTallySheet(@RequestParam("name") String name) {
        final TallyResult response = client.createNewTallySheet(name);
        final TallySheet tallySheet = response.tallySheet().orElseThrow();

        return "redirect:/" + tallySheet.getAdminKey();
    }

    @PostMapping("/{adminKey}")
    public String incrementTallySheet(@PathVariable("adminKey") String adminKey,
            @RequestParam("description") String description,
            @RequestParam("incrementDateUTC") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate incrementDate) {
        final TallyIncrement increment = new TallyIncrement();
        final LocalDateTime incrementDateUTC = LocalDateTime.of(incrementDate, LocalTime.now());
        increment.setDescription(description);
        increment.setTags(new HashSet<>());
        increment.setIncrementDateUTC(incrementDateUTC);
        final TallyResult response = client.increment(adminKey, increment);
        final TallySheet tallySheet = response.tallySheet().orElseThrow();
        return "redirect:/" + tallySheet.getAdminKey();
    }

    @GetMapping("/{key}")
    public ModelAndView showTallySheet(@PathVariable("key") String key) {
        final TallyResult response = client.getTallySheet(key);
        final TallySheet tallySheet = response.tallySheet().orElseThrow();
        final Graph graph = Graph.fromHistory(tallySheet.getIncrements());
        return new ModelAndView("tally", ImmutableMap.of("tally", tallySheet, "graph", graph));
    }

    @GetMapping(path = "/{key}/increment/{incrementId}", params = "action=delete")
    public String deleteIncrement(@PathVariable String key, @PathVariable String incrementId) {
        client.deleteIncrement(key, incrementId);
        return "redirect:/" + key;
    }
}
