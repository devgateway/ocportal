package org.devgateway.ocds.web.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Octavian Ciubotaru
 */
@Configuration
@EnableConfigurationProperties(USSDProperties.class)
public class USSDConfiguration {

    @Bean
    @Qualifier("ussd")
    public MessageSource ussdMessageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:ussd");
        return source;
    }
}
