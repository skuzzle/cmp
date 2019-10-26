package de.skuzzle.tally.frontend.slice.mvc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import de.skuzzle.tally.frontend.auth.TallyUser;
import de.skuzzle.tally.frontend.auth.TestUserConfigurer;

@Profile("slice.mvc")
@TestConfiguration
public class AuthenticationMockTestConfiguration {

    @MockBean
    TallyUser currentUser;

    @Bean
    public TestUserConfigurer testUserConfigurer() {
        return new TestUserConfigurer(currentUser);
    }
}
