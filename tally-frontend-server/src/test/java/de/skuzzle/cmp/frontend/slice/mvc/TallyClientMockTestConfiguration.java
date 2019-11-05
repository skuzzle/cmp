package de.skuzzle.cmp.frontend.slice.mvc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import de.skuzzle.cmp.frontend.client.BackendClient;
import de.skuzzle.cmp.frontend.client.TestTallyClientConfigurer;

@Profile("slice.mvc")
@TestConfiguration
public class TallyClientMockTestConfiguration {

    @MockBean
    private BackendClient tallyClient;

    @Bean
    public TestTallyClientConfigurer testTallyClientConfigurer() {
        return new TestTallyClientConfigurer();
    }

}
