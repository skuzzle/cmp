package de.skuzzle.cmp.rest.ratelimit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.skuzzle.cmp.common.http.RequestId;
import de.skuzzle.cmp.common.ratelimit.RateLimitExceededException;

@ControllerAdvice
public class RateLimitExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<RestErrorMessage> onRateLimitExceeded(RateLimitExceededException e) {
        final String requestId = RequestId.forCurrentThread();
        final RestErrorMessage body = RestErrorMessage.of(e.getMessage(), e.getClass().getSimpleName(), requestId);
        return new ResponseEntity<>(body, HttpStatus.TOO_MANY_REQUESTS);
    }
}
