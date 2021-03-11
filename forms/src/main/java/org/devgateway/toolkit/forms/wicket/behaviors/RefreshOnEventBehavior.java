package org.devgateway.toolkit.forms.wicket.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;
import org.devgateway.toolkit.forms.wicket.events.AjaxUpdateEvent;

/**
 * Refresh the attached component when it receives an event with a payload that extends from {@link AjaxUpdateEvent}.
 *
 * @author Octavian Ciubotaru
 */
public final class RefreshOnEventBehavior<T extends AjaxUpdateEvent> extends Behavior {

    private final Class<T> payloadClass;

    public RefreshOnEventBehavior(Class<T> payloadClass) {
        this.payloadClass = payloadClass;
    }

    @Override
    public void bind(Component component) {
        super.bind(component);
        component.setOutputMarkupId(true);
    }

    @Override
    public void onEvent(Component component, IEvent<?> event) {
        super.onEvent(component, event);

        if (payloadClass.isAssignableFrom(event.getPayload().getClass())) {
            event.dontBroadcastDeeper();
            ((AjaxUpdateEvent) event.getPayload()).getAjaxRequestTarget().add(component);
        }
    }
}
