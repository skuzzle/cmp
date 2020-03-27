package de.skuzzle.cmp.counter.client;

import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.counter.client.TestResponses.TallySheetResponseBuilder;

class ClientTestContext {

    private final static ThreadLocal<ClientTestContext> contexts = new ThreadLocal<>();

    public static ClientTestContext initContext(TestContext testContext) {
        final BackendClient tallyClientMock = testContext.getApplicationContext().getBean(BackendClient.class);
        final ClientTestContext context = new ClientTestContext(tallyClientMock);
        contexts.set(context);
        return context;
    }

    public static ClientTestContext getContext() {
        return contexts.get();
    }

    public static void cleanContext() {
        contexts.set(null);
    }

    private final BackendClient tallyClientMock;
    private TallySheetResponseBuilder adminTallySheet;

    ClientTestContext(BackendClient tallyClientMock) {
        this.tallyClientMock = tallyClientMock;
    }

    public BackendClient getTallyClientMock() {
        return this.tallyClientMock;
    }

    public ClientTestContext configureMetaInfoResponse(RestTallyMetaInfoResponse response) {
        when(tallyClientMock.getMetaInfo()).thenReturn(response);
        return this;
    }

    public ClientTestContext configureAdminReply(Consumer<TallySheetResponseBuilder> tallySheet) {
        this.adminTallySheet = TestResponses.tallySheet();
        tallySheet.accept(adminTallySheet);
        when(tallyClientMock.getTallySheet(adminTallySheet.getAdminKey(), Filter.all()))
                .thenReturn(adminTallySheet.toResponse());
        return this;
    }

    public ClientTestContext configureClientErrorReply(String key, HttpStatus status) {
        Preconditions.checkArgument(status.is4xxClientError(), "Expected a Http Status in 4xx range, but got %s",
                status);
        final HttpClientErrorException exception = new HttpClientErrorException(status);
        when(tallyClientMock.getTallySheet(key, Filter.all())).thenThrow(exception);
        return this;
    }

    public ClientTestContext configureServerErrorReply(String key, HttpStatus status) {
        Preconditions.checkArgument(status.is5xxServerError(), "Expected a Http Status in 5xx range, but got %s",
                status);
        final HttpServerErrorException exception = new HttpServerErrorException(status);
        when(tallyClientMock.getTallySheet(key, Filter.all())).thenThrow(exception);
        return this;
    }

    public TallySheetResponseBuilder getAdminTallySheet() {
        return this.adminTallySheet;
    }

}
