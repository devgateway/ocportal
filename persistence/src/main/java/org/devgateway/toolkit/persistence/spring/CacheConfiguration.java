package org.devgateway.toolkit.persistence.spring;


import org.ehcache.core.EhcacheManager;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
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

//    @Bean
//    public CacheManager cacheManager2() {
//        final URL myUrl = this.getClass().getResource("/ehcache.xml");
//        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
////        CacheManager cm = CacheManagerBuilder.newCacheManager(xmlConfig);
//        return new EhcacheManager(xmlConfig);
//    }
}
