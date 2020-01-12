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

import de.skuzzle.cmp.counter.client.TestTallyClientConfigurer;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ActiveProfiles("slice.mvc")
@Import({ AuthenticationMockTestConfiguration.class,
        DeviceResolverConfiguration.class,
        TallyClientMockTestConfiguration.class })
@TestExecutionListeners(mergeMode = MergeMode.MERGE_WITH_DEFAULTS, listeners = TestTallyClientConfigurer.class)
public @interface FrontendTestSlice {

}
