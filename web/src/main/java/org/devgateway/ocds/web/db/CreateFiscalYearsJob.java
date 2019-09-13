package org.devgateway.ocds.web.db;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * @author idobre
 * @since 13/09/2019
 */
@Service
public class CreateFiscalYearsJob {
    private static final Logger logger = LoggerFactory.getLogger(CreateFiscalYearsJob.class);

    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Automatically create Fiscal Years entities based on the following rules:
     * * Periods for Fiscal Year is always July 1 through June 30
     * * Naming: Year of first date/ year of last date. Example July 1, 2017 to June 30, 2018 would be 2017/2018
     * * New FY will be generated June 1
     */
    @Scheduled(cron = "0 0 12 1 6 *")
    @Transactional
    public void createFiscalYears() {
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final int nextYear = currentYear + 1;

        final String fyName = currentYear + "/" + nextYear;

        // check if we already have a FY with the same name
        final FiscalYear fyByName = fiscalYearService.findByName(fyName);

        if (fyByName != null) {
            logger.error("A FY with the following name: '"
                    + fyName + "' already exist in the system with the id: "
                    + fyByName.getId() + ". We'll not create any FY for now.");
        } else {
            logger.info("Creating FY with the following name: " + fyName);
            final FiscalYear fiscalYear = new FiscalYear();
            fiscalYear.setName(fyName);

            final Calendar cal = Calendar.getInstance();
            cal.set(currentYear, Calendar.JULY, 1);
            fiscalYear.setStartDate(cal.getTime());

            cal.set(nextYear, Calendar.JUNE, 30);
            fiscalYear.setEndDate(cal.getTime());

            fiscalYearService.saveAndFlush(fiscalYear);

            // clear cache
            cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
        }
    }
}
