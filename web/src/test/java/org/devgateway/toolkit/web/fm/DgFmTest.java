/**
 *
 */
package org.devgateway.toolkit.web.fm;

import org.devgateway.toolkit.web.AbstractWebTest;
import org.devgateway.toolkit.web.fm.service.DgFmService;
import org.junit.Assert;
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
        Assert.assertTrue("Must have at least one feature", fmService.featuresCount() > 0);
        Assert.assertNotNull(fmService.getFeature("testField"));
        Assert.assertFalse(fmService.getFeature("testField").getEnabled());
        Assert.assertFalse(fmService.getFeature("testField").getVisible());
    }

    @Test
    public void testMandatory() {
        Assert.assertFalse(fmService.getFeature("field").getMandatory());
        Assert.assertTrue(fmService.getFeature("testField").getMandatory());
        Assert.assertEquals(FmConstants.DEFAULT_MANDATORY,
                fmService.getFeature("tenderField").getMandatory());
    }

    @Test
    public void testEnabled() {
        Assert.assertFalse(fmService.getFeature("field").getEnabled());
        Assert.assertFalse(fmService.getFeature("testField").getEnabled());
        Assert.assertEquals(FmConstants.DEFAULT_ENABLED, fmService.getFeature("tenderChart").getEnabled());
    }

    @Test
    public void testVisible() {
        Assert.assertFalse(fmService.getFeature("field").getEnabled());
        Assert.assertFalse(fmService.getFeature("testField").getEnabled());
        Assert.assertEquals(FmConstants.DEFAULT_VISIBLE, fmService.getFeature("tenderChart").getEnabled());
    }


    @Test
    public void testHardDeps() {
        Assert.assertTrue( fmService.getFeature("tenderChart").getHardDeps().contains("tenderField"));
        Assert.assertTrue( fmService.getFeature("tenderChart").getHardDeps().contains("tender"));
    }
}
