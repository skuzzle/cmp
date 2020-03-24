package de.skuzzle.cmp.counter.client;

import java.util.function.Consumer;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import de.skuzzle.cmp.counter.client.TestResponses.TallySheetResponseBuilder;

public class TestTallyClientConfigurer implements TestExecutionListener {

    private static <T> Consumer<T> defaultConfig() {
        return t -> {
        };
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        ClientTestContext.initContext(testContext)
                .configureAdminReply(defaultConfig())
                // .configureShare(defaultConfig())
                .configureMetaInfoResponse(RestTallyMetaInfoResponse.of(5));
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ClientTestContext.cleanContext();
    }

    public TestTallyClientConfigurer configureShare(Consumer<TallySheetResponseBuilder> tallySheet) {
        ClientTestContext.getContext().configureShare(tallySheet);
        return this;
    }

    public TestTallyClientConfigurer configureAdminReply(Consumer<TallySheetResponseBuilder> tallySheet) {
        ClientTestContext.getContext().configureAdminReply(tallySheet);
        return this;
    }

    public TestTallyClientConfigurer configureClientErrorReply(String key, HttpStatus status) {
        ClientTestContext.getContext().configureClientErrorReply(key, status);
        return this;
    }

    public TestTallyClientConfigurer configureServerErrorReply(String key, HttpStatus status) {
        ClientTestContext.getContext().configureServerErrorReply(key, status);
        return this;
    }

    public BackendClient getClient() {
        return ClientTestContext.getContext().getTallyClientMock();
    }

    public String getAdminKey() {
        return ClientTestContext.getContext().getAdminTallySheet().getAdminKey();
    }

    public BackendClient verify() {
        return Mockito.verify(getClient());
    }
}
