package org.devgateway.toolkit.persistence.validator;

import org.devgateway.toolkit.persistence.fm.FmReconfiguredEvent;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Configuration;

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
    public void setFmConstraintsConfigurer(FMConstraintsConfigurer fmConstraintsConfigurer) {
        this.fmConstraintsConfigurer = fmConstraintsConfigurer;
    }

    private FMConstraintsConfigurer fmConstraintsConfigurer;

    public HibernateValidatorFactoryBean() {
        setProviderClass(HibernateValidator.class);
    }

    @EventListener
    public void handleFmReconfiguration(FmReconfiguredEvent e) {
        afterPropertiesSet();
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
