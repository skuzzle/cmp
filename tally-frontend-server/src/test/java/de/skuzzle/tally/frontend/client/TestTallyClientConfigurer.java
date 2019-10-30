package de.skuzzle.tally.frontend.client;

import java.util.function.Consumer;

import org.mockito.Mockito;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import de.skuzzle.tally.frontend.client.TestResponses.TallySheetResponseBuilder;

public class TestTallyClientConfigurer implements TestExecutionListener {

    private static <T> Consumer<T> defaultConfig() {
        return t -> {
        };
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        ClientTestContext.initContext(testContext)
                .configureAdminReply(defaultConfig())
                .configurePublic(defaultConfig())
                .configureMetaInfoResponse(RestTallyMetaInfoResponse.of(5));
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ClientTestContext.cleanContext();
    }

    public TestTallyClientConfigurer configurePublic(Consumer<TallySheetResponseBuilder> tallySheet) {
        ClientTestContext.getContext().configurePublic(tallySheet);
        return this;
    }

    public TestTallyClientConfigurer configureAdminReply(Consumer<TallySheetResponseBuilder> tallySheet) {
        ClientTestContext.getContext().configureAdminReply(tallySheet);
        return this;
    }

    public BackendClient getClient() {
        return ClientTestContext.getContext().getTallyClientMock();
    }

    public String getPublicKey() {
        return ClientTestContext.getContext().getPublicTallySheet().getPublicKey();
    }

    public String getAdminKey() {
        return ClientTestContext.getContext().getAdminTallySheet().getAdminKey();
    }

    public BackendClient verify() {
        return Mockito.verify(getClient());
    }
}
