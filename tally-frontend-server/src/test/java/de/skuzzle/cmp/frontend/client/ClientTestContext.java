package de.skuzzle.cmp.frontend.client;

import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.springframework.test.context.TestContext;

import de.skuzzle.cmp.frontend.client.BackendClient;
import de.skuzzle.cmp.frontend.client.RestTallyMetaInfoResponse;
import de.skuzzle.tally.frontend.client.TestResponses.TallySheetResponseBuilder;

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
    private TallySheetResponseBuilder publicTallySheet;
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

    public ClientTestContext configurePublic(Consumer<TallySheetResponseBuilder> tallySheet) {
        this.publicTallySheet = TestResponses.tallySheet().withAdminKey(null);
        tallySheet.accept(publicTallySheet);
        when(tallyClientMock.getTallySheet(publicTallySheet.getPublicKey())).thenReturn(publicTallySheet.toResponse());
        return this;
    }

    public ClientTestContext configureAdminReply(Consumer<TallySheetResponseBuilder> tallySheet) {
        this.adminTallySheet = TestResponses.tallySheet();
        tallySheet.accept(adminTallySheet);
        when(tallyClientMock.getTallySheet(adminTallySheet.getAdminKey())).thenReturn(adminTallySheet.toResponse());
        return this;
    }

    public TallySheetResponseBuilder getAdminTallySheet() {
        return this.adminTallySheet;
    }

    public TallySheetResponseBuilder getPublicTallySheet() {
        return this.publicTallySheet;
    }
}
