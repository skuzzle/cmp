package de.skuzzle.tally.service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TallyRestController {

    private final TallyService tallyService;

    public TallyRestController(TallyService tallyService) {
        this.tallyService = tallyService;
    }

    @GetMapping("/public/{key}")
    public TallySheet getTally(@PathVariable String key) {
        return tallyService.getTallySheet(key);
    }

    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public TallySheet createTally(@PathVariable @NotEmpty String name) {
        return tallyService.createNewTallySheet(name);
    }

    @PostMapping("/admin/{key}")
    @ResponseStatus(HttpStatus.OK)
    public TallySheet increment(@PathVariable String key, @RequestBody @Valid TallyIncrement increment) {
        return tallyService.increment(key, increment);
    }

    @DeleteMapping("/admin/{key}")
    public void deleteTallySheet(@PathVariable String key) {
        tallyService.deleteTallySheet(key);
    }
}
