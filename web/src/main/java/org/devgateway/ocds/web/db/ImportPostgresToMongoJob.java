package org.devgateway.ocds.web.db;

import org.devgateway.ocds.web.convert.OCPortalToOCDSConversionService;
import org.devgateway.ocds.web.spring.ReleaseFlaggingService;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.web.rest.controller.alerts.processsing.AlertsManager;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author idobre
 * @since 2019-07-08
 */
@Service
public class ImportPostgresToMongoJob {

    @Autowired
    private ImportPostgresToMongo importPostgresToMongo;

    @Autowired
    private OCPortalToOCDSConversionService ocPortalToOCDSConversionService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ReleaseFlaggingService releaseFlaggingService;

    @Autowired
    private AlertsManager alertsManager;

    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    public void formStatusIntegrityCheck() {
        importPostgresToMongo.formStatusIntegrityCheck();
    }

    /**
     * Invoke the import of all makueni data into mongo db.
     */
    @Scheduled(cron = "0 0 23 * * SAT")
    @Async
    public Future<String> importOcdsClientToMongo() {
        formStatusIntegrityCheck();
        importPostgresToMongo.importToMongo();
        ocPortalToOCDSConversionService.convertToOcdsAndSaveAllApprovedPurchaseRequisitions();
        releaseFlaggingService.processAndSaveFlagsForAllReleases(releaseFlaggingService::logMessage);
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());

        if (!SecurityUtil.getDisableEmailAlerts(adminSettingsRepository)) {
            alertsManager.sendAlerts();
        }
        return new AsyncResult<>("import completed");
    }
}
