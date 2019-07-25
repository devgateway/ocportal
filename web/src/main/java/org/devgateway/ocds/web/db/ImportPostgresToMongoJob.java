package org.devgateway.ocds.web.db;

import org.devgateway.ocds.web.convert.MakueniToOCDSConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author idobre
 * @since 2019-07-08
 */
@Service
public class ImportPostgresToMongoJob {

    @Autowired
    private ImportPostgresToMongo importPostgresToMongo;

    @Autowired
    private MakueniToOCDSConversionService makueniToOCDSConversionService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Invoke the import of all makueni data into mongo db.
     */
    @Scheduled(cron = "0 0 23 * * SAT")
    @Async
    public void importOcdsMakueniToMongo() {
        importPostgresToMongo.importToMongo();
        makueniToOCDSConversionService.convertToOcdsAndSaveAllApprovedPurchaseRequisitions();
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
    }
}
