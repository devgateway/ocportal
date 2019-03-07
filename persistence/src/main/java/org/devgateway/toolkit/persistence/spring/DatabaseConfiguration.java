/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.persistence.spring;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.persistence.EntityManagerFactory;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@Profile("!integration")
public class DatabaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    public SpringLiquibaseRunner liquibaseAfterJPA(final SpringLiquibase springLiquibase,
                                                   final EntityManagerFactory entityManagerFactory) {
        logger.info("Instantiating SpringLiquibaseRunner after initialization of entityManager using factory "
                + entityManagerFactory);
        return new SpringLiquibaseRunner(springLiquibase);
    }

}
