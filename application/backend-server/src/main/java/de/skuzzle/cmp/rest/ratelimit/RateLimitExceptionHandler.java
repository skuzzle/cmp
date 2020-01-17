package de.skuzzle.cmp.rest.ratelimit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.skuzzle.cmp.common.ratelimit.RateLimitExceededException;

@ControllerAdvice
public class RateLimitExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<RestErrorMessage> onRateLimitExceeded(RateLimitExceededException e) {
        final RestErrorMessage body = RestErrorMessage.of(e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(body, HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }
}
