/**
 *
 */
package org.devgateway.toolkit.web.fm;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.devgateway.toolkit.persistence.fm.entity.DgFeature;
import org.devgateway.toolkit.web.AbstractWebTest;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

/**
 * @author mihai
 *
 */
@TestPropertySource("classpath:testFm.properties")
public class DgFmTest extends AbstractWebTest {

    @Autowired
    private DgFmService fmService;

    @Test
    public void testFmInit() {
        assertTrue("Must have at least one feature", fmService.featuresCount() > 0);
    }

    @Test
    public void testBasics() {
        assertThatFeature("defaults", allOf(visible(), enabled(), not(mandatory())));
        assertThatFeature("allEnabled", allOf(visible(), enabled(), mandatory()));
        assertThatFeature("allDisabled", allOf(not(visible()), not(enabled()), not(mandatory())));
        assertThatFeature("reverseDefaults", allOf(not(visible()), not(enabled()), mandatory()));
    }

    @Test
    public void testMixins() {
        assertThatFeature("allDisabledMixin", allOf(not(visible()), not(enabled()), not(mandatory())));
        assertThatFeature("mandatoryWithAllDisabledMixin", allOf(not(visible()), not(enabled()), mandatory()));
        assertThatFeature("mandatoryWithAllDisabledChainedMixin", allOf(not(visible()), not(enabled()), mandatory()));
        assertThatFeature("mixingInAllEnabled", allOf(not(visible()), not(enabled()), mandatory()));
    }

    @Test
    public void testVisibleDeps() {
        assertThatFeature("visibleDepsAllDisabled", allOf(not(visible()), enabled(), not(mandatory())));
        assertThatFeature("visibleDepsAllDisabledChained", allOf(not(visible()), enabled(), not(mandatory())));
        assertThatFeature("visibleDepsAllEnabled", allOf(not(visible()), enabled(), not(mandatory())));
    }

    @Test
    public void testMandatoryDeps() {
        assertThatFeature("mandatoryDepsAllDisabled", allOf(visible(), enabled(), mandatory()));
        assertThatFeature("mandatoryDepsAllEnabled", allOf(visible(), enabled(), mandatory()));
        assertThatFeature("mandatoryDepsAllEnabledChained", allOf(visible(), enabled(), mandatory()));
    }

    @Test
    public void testEnabledDeps() {
        assertThatFeature("enabledDepsAllDisabled", allOf(visible(), not(enabled()), not(mandatory())));
        assertThatFeature("enabledDepsAllDisabledChained", allOf(visible(), not(enabled()), not(mandatory())));
        assertThatFeature("enabledDepsAllEnabled", allOf(visible(), not(enabled()), not(mandatory())));
    }

    private void assertThatFeature(String featureName, Matcher<DgFeature> matcher) {
        assertThat(fmService.getFeature(featureName), matcher);
    }

    private void assertFeature(String featureName, boolean visible, boolean enabled, boolean mandatory) {
        assertEquals(fmService.isFeatureVisible(featureName), visible);
        assertEquals(fmService.isFeatureEnabled(featureName), enabled);
        assertEquals(fmService.isFeatureMandatory(featureName), mandatory);
    }

    private Matcher<DgFeature> visible() {
        return describedAs("visible", hasProperty("visible", equalTo(true)));
    }

    private Matcher<DgFeature> mandatory() {
        return describedAs("mandatory", hasProperty("mandatory", equalTo(true)));
    }

    private Matcher<DgFeature> enabled() {
        return describedAs("enabled", hasProperty("enabled", equalTo(true)));
    }
}
