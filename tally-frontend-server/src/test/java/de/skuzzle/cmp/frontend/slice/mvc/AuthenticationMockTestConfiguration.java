package de.skuzzle.cmp.frontend.slice.mvc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import de.skuzzle.cmp.auth.TallyUser;
import de.skuzzle.cmp.auth.TestUserConfigurer;

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
