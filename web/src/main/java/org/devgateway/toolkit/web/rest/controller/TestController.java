package org.devgateway.toolkit.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.db.CreateFiscalYearsJob;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author idobre
 * @since 10/16/16
 */

@RestController
@CacheConfig(cacheNames = "reportsApiCache")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private CreateFiscalYearsJob createFiscalYearsJob;

    @Autowired
    private FiscalYearService fiscalYearService;

    @ApiOperation(value = "Test API")
    @RequestMapping(value = "/api/testAPI", method = RequestMethod.GET, produces = "application/json")
    @Cacheable
    public String testAPI() {
        String responseJson = "{\n"
                + "    \"columnNames\": [\"Col 1\", \"Col 2\"],\n"
                + "    \"data\": [[\"aaa\", \"bbb\"], [\"ccc\", \"ddd\"], [\"eee\", \"fff\"]]\n"
                + "}";

        logger.error(">>> responseJson: " + responseJson);

        return responseJson;
    }


    @ApiOperation(value = "Test FY creation")
    @RequestMapping(value = "/api/createFY", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public String createFY() {
        final long countBefore = fiscalYearService.count();
        createFiscalYearsJob.createFiscalYears();
        final long countAfter = fiscalYearService.count();

        if (countBefore != countAfter) {
            return "A new FY has been created!";
        } else {
            return "No new FY was created! Probably there is already one with the same name.";
        }
    }
}
