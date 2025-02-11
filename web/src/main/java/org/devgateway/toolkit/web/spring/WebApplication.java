/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.web.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * @author mpostelnicu
 */

@SpringBootApplication
@PropertySource("classpath:/org/devgateway/toolkit/web/application.properties")
@ComponentScan(value = "org.devgateway.toolkit", excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = org.devgateway.jocds.ValidatorConfiguration.class))
public class WebApplication {

    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(final String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

//    @Bean
//    public SpringLiquibaseRunner liquibaseAfterJPA(final SpringLiquibase springLiquibase,
//                                                   final EntityManagerFactory entityManagerFactory) {
//        logger.info("Instantiating SpringLiquibaseRunner after initialization of entityManager using factory "
//                + entityManagerFactory);
//        return new SpringLiquibaseRunner(springLiquibase);
//    }
}
