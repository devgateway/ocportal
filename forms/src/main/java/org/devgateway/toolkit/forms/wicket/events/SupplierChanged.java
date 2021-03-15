package org.devgateway.toolkit.forms.wicket.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author Octavian Ciubotaru
 */
public final class SupplierChanged extends AjaxUpdateEvent {

    public SupplierChanged(AjaxRequestTarget target) {
        super(target);
    }
}
