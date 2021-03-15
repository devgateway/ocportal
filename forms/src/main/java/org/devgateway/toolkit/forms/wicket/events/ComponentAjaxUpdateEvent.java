package org.devgateway.toolkit.forms.wicket.events;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class ComponentAjaxUpdateEvent extends AjaxUpdateEvent {

    private final String componentId;

    public ComponentAjaxUpdateEvent(final AjaxRequestTarget target, Component component) {
        super(target);
        componentId = component.getId();
    }

    public final String getComponentId() {
        return componentId;
    }
}
