package de.skuzzle.cmp.users.security;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityAssert<T> extends AbstractAssert<ResponseEntityAssert<T>, ResponseEntity<T>> {

    public ResponseEntityAssert(ResponseEntity<T> actual) {
        super(actual, ResponseEntityAssert.class);
    }

    public static <T> ResponseEntityAssert<T> assertThat(ResponseEntity<T> actual) {
        return new ResponseEntityAssert<>(actual);
    }

    public ResponseEntityAssert<T> hasStatus(HttpStatus status) {
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(status);
        return this;
    }

    public ListAssert<String> hasHeader(String expectedHeaderName) {
        final List<String> headerValues = actual.getHeaders().get(expectedHeaderName);
        return Assertions.assertThat(headerValues).isNotEmpty();
    }

    public ResponseEntityAssert<T> isRedirectTo(String expectedUrl) {
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        hasHeader("Location").contains(expectedUrl);
        return this;
    }
}
