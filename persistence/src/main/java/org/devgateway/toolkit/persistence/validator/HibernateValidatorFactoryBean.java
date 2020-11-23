package org.devgateway.toolkit.persistence.validator;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Configuration;

/**
 * Configure and expose a bean validator based on {@link HibernateValidator}.
 *
 * Also configures and constraints defined in FM, see {@link FMConstraintsConfigurer}.
 *
 * @author Octavian Ciubotaru
 */
@Component
public class HibernateValidatorFactoryBean extends LocalValidatorFactoryBean {

    @Autowired
    private FMConstraintsConfigurer fmConstraintsConfigurer;

    public HibernateValidatorFactoryBean() {
        setProviderClass(HibernateValidator.class);
    }

    @Override
    protected void postProcessConfiguration(Configuration<?> genericConfiguration) {
        super.postProcessConfiguration(genericConfiguration);

        addConstraintMapping(genericConfiguration);
    }

    private void addConstraintMapping(Configuration<?> genericConfiguration) {
        HibernateValidatorConfiguration configuration = (HibernateValidatorConfiguration) genericConfiguration;

        fmConstraintsConfigurer.configure(configuration);
    }
}
