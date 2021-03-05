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
package org.devgateway.toolkit.forms.wicket.events;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

public class AjaxUpdateEvent {
    private final AjaxRequestTarget target;
    private List<String> componentIds = null;

    public AjaxUpdateEvent(final AjaxRequestTarget target, String... componentIds) {
        this.target = target;
        if (!ObjectUtils.isEmpty(componentIds)) {
            this.componentIds = Arrays.asList(componentIds);
        }
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return target;
    }

    public boolean isForComponentId(String componentId) {
        return componentIds != null && componentIds.contains(componentId);
    }

    public static void refreshIfPayloadMatches(IEvent<?> event, Component component, String id) {
        if (event.getPayload() instanceof AjaxUpdateEvent
                && ((AjaxUpdateEvent) event.getPayload()).isForComponentId(id)) {
            ((AjaxUpdateEvent) event.getPayload()).getAjaxRequestTarget().add(component);
        }
    }
}