package de.skuzzle.cmp.counter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.mobile.device.annotation.DeviceResolverConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.TestPropertySource;

import de.skuzzle.cmp.counter.client.TestTallyClientConfigurer;
import de.skuzzle.cmp.counter.version.VersionSpringConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ActiveProfiles("slice.mvc")
@Import({ AuthenticationMockTestConfiguration.class,
        DeviceResolverConfiguration.class,
        TallyClientMockTestConfiguration.class,
        VersionSpringConfiguration.class })
@TestExecutionListeners(mergeMode = MergeMode.MERGE_WITH_DEFAULTS, listeners = TestTallyClientConfigurer.class)
@TestPropertySource(properties = "cmp.version=1")
public @interface FrontendTestSlice {

}
