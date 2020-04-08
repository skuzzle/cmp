package de.skuzzle.cmp.counter.frontpage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.ModelAndView;

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.counter.client.BackendClient;
import de.skuzzle.cmp.counter.client.RestTallyMetaInfoResponse;
import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.RestTallySheet;
import de.skuzzle.cmp.counter.client.RestTallySheetsReponse;
import de.skuzzle.cmp.counter.graphs.Graph;
import de.skuzzle.cmp.ui.socialcard.SocialCard;

@Controller
public class FrontpageController {

    private final BackendClient client;
    private final TallyUser currentUser;

    public FrontpageController(BackendClient client, TallyUser currentUser) {
        this.client = client;
        this.currentUser = currentUser;
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ModelAndView handleClientError(HttpStatusCodeException e) {
        final ModelAndView modelAndView = new ModelAndView();
        final HttpStatus statusCode = e.getStatusCode();
        if (statusCode == HttpStatus.NOT_FOUND) {
            modelAndView.setViewName("error/404");
        } else {
            modelAndView.setViewName("error");
        }
        modelAndView.addObject("statusCode", statusCode);
        modelAndView.setStatus(statusCode);
        return modelAndView;
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
        final SocialCard socialCard = SocialCard.withTitle("Count My Pizza")
                .withDescription("Count pizzas - and more!")
                .build();

        final ModelAndView model = new ModelAndView("frontpage/frontPage.html")
                .addObject("totalTallySheetCount", metaInfo.getTotalTallySheetCount())
                .addObject("recentlyCreated", List.of())
                .addObject("socialCard", socialCard);

        if (user.isLoggedIn()) {
            final RestTallySheetsReponse result = client.listTallySheets();
            final List<RestTallySheet> sheets = result.getTallySheets();
            final List<RecentlyCreatedTally> recentlyCreated = sheets.stream()
                    .sorted(Comparator.comparing(RestTallySheet::getLastModifiedDateUTC).reversed())
                    .map(RecentlyCreatedTally::fromRestResponse)
                    .collect(Collectors.toList());
            model.addObject("recentlyCreated", recentlyCreated);
        }
        return model;
    }

    @PostMapping("/counter/_create")
    public String createTallySheet(@RequestParam("name") String name) {
        final RestTallyResponse response = client.createNewTallySheet(name);
        return "redirect:/counter/" + response.getTallySheet().getAdminKey();
    }

    @GetMapping(path = "/counter/{key}", params = "action=delete")
    public String deleteTallySheet(@PathVariable String key) {
        client.deleteTallySheet(key);
        return "redirect:/";
    }

}
