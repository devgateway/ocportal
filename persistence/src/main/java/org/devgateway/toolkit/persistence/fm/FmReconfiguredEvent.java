package org.devgateway.toolkit.persistence.fm;

import org.springframework.context.ApplicationEvent;

/**
 * @author Octavian Ciubotaru
 */
public class FmReconfiguredEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public FmReconfiguredEvent(Object source) {
        super(source);
    }
}
