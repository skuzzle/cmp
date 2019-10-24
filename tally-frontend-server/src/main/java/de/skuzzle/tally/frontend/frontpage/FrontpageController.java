package de.skuzzle.tally.frontend.frontpage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.skuzzle.tally.frontend.auth.TallyUser;
import de.skuzzle.tally.frontend.client.RestTallyResponse;
import de.skuzzle.tally.frontend.client.RestTallySheet;
import de.skuzzle.tally.frontend.client.RestTallySheetsReponse;
import de.skuzzle.tally.frontend.client.TallyClient;
import de.skuzzle.tally.frontend.client.TallyResult;
import de.skuzzle.tally.frontend.graphs.Graph;

@Controller
public class FrontpageController {

    private final TallyClient client;
    private final TallyUser currentUser;

    public FrontpageController(TallyClient client, TallyUser currentUser) {
        this.client = client;
        this.currentUser = currentUser;
    }

    @ModelAttribute("user")
    public TallyUser getUser() {
        return currentUser;
    }

    @ModelAttribute("exampleGraph")
    public Graph exampleGraph() {
        return ExampleGraph.randomGraph();
    }

    @GetMapping("/")
    public ModelAndView getIndex() {
        final TallyUser user = getUser();
        final ModelAndView model = new ModelAndView("frontpage/frontPage.html")
                .addObject("recentlyCreated", List.of());
        if (user.isLoggedIn()) {
            final TallyResult<RestTallySheetsReponse> result = client.listTallySheets();
            if (result.isSuccess()) {
                final List<RestTallySheet> sheets = result.payload().map(RestTallySheetsReponse::getTallySheets)
                        .orElseThrow();
                final List<RecentlyCreatedTally> recentlyCreated = sheets.stream()
                        .sorted(Comparator.comparing(RestTallySheet::getCreateDateUTC).reversed())
                        .map(RecentlyCreatedTally::fromRestResponse)
                        .collect(Collectors.toList());
                model.addObject("recentlyCreated", recentlyCreated);
            }
        }
        return model;
    }

    @PostMapping("/_create")
    public String createTallySheet(@RequestParam("name") String name) {
        final RestTallySheet tallySheet = client.createNewTallySheet(name)
                .payload()
                .map(RestTallyResponse::getTallySheet)
                .orElseThrow();

        return "redirect:/" + tallySheet.getAdminKey();
    }

    @GetMapping(path = "/{key}", params = "action=delete")
    public String deleteTallySheet(@PathVariable String key) {
        client.deleteTallySheet(key);
        return "redirect:/";
    }

}
