package org.devgateway.toolkit.forms.util;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author idobre
 * @since 3/3/16
 * <p>
 * Class that removes the cache created in
 * org.devgateway.ccrs.web.wicket.page.reports.AbstractReportPage#ResourceStreamPanel#getCacheKey
 * function
 */
@Component
public class MarkupCacheService {
    protected static final Logger logger = LoggerFactory.getLogger(MarkupCacheService.class);

    /**
     * start-key used to identify the reports markup
     */
    private static final String START_NAME_REPORT_KEY = "REPORTMARKUP";

    /**
     * Flush markup cache for reports page
     */
    public final void flushMarkupCache() {
        final MarkupCache markupCacheClass = (MarkupCache) MarkupCache.get();
        final MarkupCache.ICache<String, Markup> markupCache = markupCacheClass.getMarkupCache();
        final Collection<String> keys = markupCache.getKeys();
        for (String key : keys) {
            // The key for reports markup cache contains the class name so it
            // should end in "ReportPage"
            if (key.startsWith(START_NAME_REPORT_KEY)) {
                markupCacheClass.removeMarkup(key);
            }
        }
    }

    /**
     * Add the content of a report (PDF, Excel, RTF) to cache
     *
     * @param outputType
     * @param reportName
     * @param parameters
     * @param buffer
     */
    public void addPentahoReportToCache(final String outputType, final String reportName, final String parameters,
                                        final byte[] buffer) {
        // Initialize CacheManager using Ehcache 3.x
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("reportsCache", CacheConfigurationBuilder.
                        newCacheConfigurationBuilder(String.class, byte[].class,
                        ResourcePoolsBuilder.heap(100)))
                .build(true);

        // Retrieve the "reportsCache" cache
        Cache<String, byte[]> cache = cacheManager.getCache("reportsCache", String.class, byte[].class);

        // Put the report content into the cache
        String cacheKey = createCacheKey(outputType, reportName, parameters);
        cache.put(cacheKey, buffer);
    }

    /**
     * Fetch the content of a report from cache
     *
     * @param outputType
     * @param reportName
     * @param parameters
     * @return
     */
    public byte[] getPentahoReportFromCache(final String outputType, final String reportName, final String parameters) {
        // Initialize CacheManager using Ehcache 3.x
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("reportsCache", CacheConfigurationBuilder.
                        newCacheConfigurationBuilder(String.class, byte[].class,
                        ResourcePoolsBuilder.heap(100)))
                .build(true);

        // Retrieve the "reportsCache" cache
        Cache<String, byte[]> cache = cacheManager.getCache("reportsCache", String.class, byte[].class);

        String key = createCacheKey(outputType, reportName, parameters);

        // Fetch the content from the cache
        return cache.containsKey(key) ? cache.get(key) : null;
    }

    /**
     * Remove from cache all reports content
     */
    public void clearPentahoReportsCache() {
        // Initialize CacheManager using Ehcache 3.x
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("reportsCache", CacheConfigurationBuilder.
                        newCacheConfigurationBuilder(String.class, byte[].class,
                        ResourcePoolsBuilder.heap(100)))
                .build(true);

        // Retrieve the "reportsCache" cache
        Cache<String, byte[]> cache = cacheManager.getCache("reportsCache", String.class, byte[].class);

        // Clear the cache
        cache.clear();
    }

    /**
     * Remove from cache all APIs/Services content.
     */
    public void clearAllCaches() {
        // Initialize CacheManager using Ehcache 3.x
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("reportsApiCache", CacheConfigurationBuilder.
                        newCacheConfigurationBuilder(String.class, byte[].class,
                        ResourcePoolsBuilder.heap(100)))
                .withCache("excelExportCache", CacheConfigurationBuilder.
                        newCacheConfigurationBuilder(String.class, byte[].class,
                        ResourcePoolsBuilder.heap(100)))
                .build(true);

        // Retrieve the caches and clear them
        Cache<String, byte[]> reportsApiCache = cacheManager.
                getCache("reportsApiCache", String.class, byte[].class);
        if (reportsApiCache != null) {
            reportsApiCache.clear();
        }

        Cache<String, byte[]> excelExportCache = cacheManager.
                getCache("excelExportCache", String.class, byte[].class);
        if (excelExportCache != null) {
            excelExportCache.clear();
        }
    }

    private String createCacheKey(final String outputType, final String reportName,
                                  final String parameters) {
        return reportName + "-" + parameters + "-" + outputType;
    }
}
