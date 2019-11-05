package de.skuzzle.cmp.frontend.slice.mvc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Profile("slice.mvc")
@TestConfiguration
class FrontendTestConfiguration {

    // somehow required because of type exclusions performed by @WebMvcTests
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

}
