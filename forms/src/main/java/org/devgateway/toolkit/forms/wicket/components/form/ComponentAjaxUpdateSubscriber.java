package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.Component;
import org.devgateway.toolkit.forms.wicket.behaviors.ComponentAjaxUpdateBehavior;

/**
 * Define subscriber behavior, adding {@link ComponentAjaxUpdateBehavior} to components
 *
 * @author mpostelnicu
 */
public interface ComponentAjaxUpdateSubscriber {

    /**
     * The component to subscribe to updates
     * @return
     */
    Component getAjaxUpdateSubscriberComponent();

    /**
     * Invoke externally to enable subscription
     *
     * @param componentId
     */
    default void receiveUpdatesFrom(String componentId) {
        getAjaxUpdateSubscriberComponent().add(new ComponentAjaxUpdateBehavior(componentId));
    }

    default void receiveUpdatesFrom(String... componentIds) {
        for (String componentId : componentIds) {
            receiveUpdatesFrom(componentId);
        }
    }
}
