package de.skuzzle.cmp.counter.frontpage;

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

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.counter.client.BackendClient;
import de.skuzzle.cmp.counter.client.RestTallyMetaInfoResponse;
import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.RestTallySheet;
import de.skuzzle.cmp.counter.client.RestTallySheetsReponse;
import de.skuzzle.cmp.counter.graphs.Graph;

@Controller
public class FrontpageController {

    private final BackendClient client;
    private final TallyUser currentUser;

    public FrontpageController(BackendClient client, TallyUser currentUser) {
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
        final RestTallyMetaInfoResponse metaInfo = client.getMetaInfo();

        final ModelAndView model = new ModelAndView("frontpage/frontPage.html")
                .addObject("totalTallySheetCount", metaInfo.getTotalTallySheetCount())
                .addObject("recentlyCreated", List.of());

        if (user.isLoggedIn()) {
            final RestTallySheetsReponse result = client.listTallySheets();
            final List<RestTallySheet> sheets = result.getTallySheets();
            final List<RecentlyCreatedTally> recentlyCreated = sheets.stream()
                    .sorted(Comparator.comparing(RestTallySheet::getCreateDateUTC).reversed())
                    .map(RecentlyCreatedTally::fromRestResponse)
                    .collect(Collectors.toList());
            model.addObject("recentlyCreated", recentlyCreated);
        }
        return model;
    }

    @PostMapping("/_create")
    public String createTallySheet(@RequestParam("name") String name) {
        final RestTallyResponse response = client.createNewTallySheet(name);
        return "redirect:/" + response.getTallySheet().getAdminKey();
    }

    @GetMapping(path = "/{key}", params = "action=delete")
    public String deleteTallySheet(@PathVariable String key) {
        client.deleteTallySheet(key);
        return "redirect:/";
    }

}
