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

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ValidatorFactory;

/**
 * @author mpostelnicu
 *
 */
@Configuration
//@Profile("!integration") // TODO check why this setting is needed.
public class DatabaseConfiguration {

    /**
     * Pass our preconfigured validator factory to Hibernate ORM.
     *
     * Important! BeanValidationEventListener will traverse only the fields that were initialized and does not
     * support cascade validation.
     */
    @Bean
    public HibernatePropertiesCustomizer validationFactoryProperty(ValidatorFactory validatorFactory) {
        return props -> {
            props.put("javax.persistence.validation.factory", validatorFactory);
        };
    }
}
