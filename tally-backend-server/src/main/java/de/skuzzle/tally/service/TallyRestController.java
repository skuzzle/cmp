package de.skuzzle.tally.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TallyRestController {

    private final TallyService tallyService;
    private final ApiRateLimiter rateLimiter;

    public TallyRestController(TallyService tallyService, ApiRateLimiter rateLimiter) {
        this.tallyService = tallyService;
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/public/{key}")
    public TallySheet getTally(@PathVariable String key) {
        return tallyService.getTallySheet(key);
    }

    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public TallySheet createTally(@PathVariable @NotEmpty String name, HttpServletRequest request) {
        rateLimiter.throttle(request);
        return tallyService.createNewTallySheet(name);
    }

    @PostMapping("/admin/{key}")
    @ResponseStatus(HttpStatus.OK)
    public TallySheet increment(@PathVariable String key, @RequestBody @Valid TallyIncrement increment, HttpServletRequest request) {
        rateLimiter.throttle(request);
        return tallyService.increment(key, increment);
    }

    @DeleteMapping("/admin/{key}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTallySheet(@PathVariable String key, HttpServletRequest request) {
        rateLimiter.throttle(request);
        tallyService.deleteTallySheet(key);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> onRateLimitExceeded(RateLimitExceededException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage(), e.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }

    @ExceptionHandler(TallySheetNotAvailableException.class)
    public ResponseEntity<ErrorResponse> onTallySheetNotAvailable(TallySheetNotAvailableException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage(), e.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    private static class ErrorResponse {
        private final String message;
        private final String origin;

        public ErrorResponse(String message, String origin) {
            this.message = message;
            this.origin = origin;
        }

        public String getMessage() {
            return message;
        }

        public String getOrigin() {
            return origin;
        }

    }

}
