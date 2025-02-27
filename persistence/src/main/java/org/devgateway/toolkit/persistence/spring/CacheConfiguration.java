package org.devgateway.toolkit.persistence.spring;


import org.ehcache.core.EhcacheManager;
import org.ehcache.xml.XmlConfiguration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;


@Configuration
@EnableCaching
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class CacheConfiguration {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer(
            final JCacheCacheManager cacheManager) {
        return (properties) -> properties.put(ConfigSettings.CACHE_MANAGER, cacheManager.getCacheManager());
    }
}
