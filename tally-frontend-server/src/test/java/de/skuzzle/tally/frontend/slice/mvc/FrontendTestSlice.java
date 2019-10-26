package de.skuzzle.tally.frontend.slice.mvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

import de.skuzzle.tally.frontend.Version;
import de.skuzzle.tally.frontend.client.TestTallyClientConfigurer;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ActiveProfiles("slice.mvc")
@EnableConfigurationProperties(Version.class)
@Import({ FrontendTestConfiguration.class, AuthenticationMockTestConfiguration.class,
        TallyClientMockTestConfiguration.class })
@TestExecutionListeners(mergeMode = MergeMode.MERGE_WITH_DEFAULTS, listeners = TestTallyClientConfigurer.class)
public @interface FrontendTestSlice {

}
