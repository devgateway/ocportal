package org.devgateway.toolkit.forms.wicket.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;
import org.devgateway.toolkit.forms.wicket.events.ComponentAjaxUpdateEvent;

/**
 * Behavior that triggers ajax update of the attached {@link Component}, if the {@link ComponentAjaxUpdateEvent}
 * was sent for this component
 *
 * @author mpostelnicu
 */
public class ComponentAjaxUpdateBehavior extends Behavior {

    private final String componentId;

    /**
     * The {@link Component#getId()} to receive ajax updates from
     * @param componentId
     */
    public ComponentAjaxUpdateBehavior(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public void onEvent(Component component, IEvent<?> event) {
        super.onEvent(component, event);
        if (event.getPayload() instanceof ComponentAjaxUpdateEvent
                && ((ComponentAjaxUpdateEvent) event.getPayload()).getComponentId().equals(componentId)) {
            ((ComponentAjaxUpdateEvent) event.getPayload()).getAjaxRequestTarget().add(component);
            event.dontBroadcastDeeper();
        }
    }
}
