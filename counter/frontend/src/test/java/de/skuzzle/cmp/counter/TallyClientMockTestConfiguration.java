package de.skuzzle.cmp.counter;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import de.skuzzle.cmp.counter.client.BackendClient;
import de.skuzzle.cmp.counter.client.TestTallyClientConfigurer;

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
