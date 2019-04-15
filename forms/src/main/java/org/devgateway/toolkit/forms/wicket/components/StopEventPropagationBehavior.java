package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;

/**
 * @author idobre
 * @since 2019-04-10
 *
 * This can be add directly on a {@link GenericBootstrapFormComponent}
 * or on {@link GenericBootstrapFormComponent#getField()}.
 */
public class StopEventPropagationBehavior extends Behavior {

    @Override
    public void onComponentTag(final Component component, final ComponentTag tag) {
        tag.put("onclick", "var event = arguments[0] || window.event; "
                + "var isIE = /Edge\\/|Trident\\/|MSIE /.test(window.navigator.userAgent); "
                + "if (!isIE) event.stopPropagation();");
    }
}
