package de.skuzzle.cmp.frontend.imprint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.skuzzle.cmp.frontend.auth.TallyUser;

@Controller
class ImprintPage {
    
    private final TallyUser currentUser;

    public ImprintPage(TallyUser currentUser) {
        this.currentUser = currentUser;
    }

    @ModelAttribute("user")
    public TallyUser getUser() {
        return currentUser;
    }

    @GetMapping("imprint")
    public String imprintPage() {
        return "imprint";
    }
}
