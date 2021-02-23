package org.devgateway.toolkit.forms.wicket.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author Octavian Ciubotaru
 */
public final class SupplierChanged {

    private final AjaxRequestTarget target;

    public SupplierChanged(AjaxRequestTarget target) {
        this.target = target;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }
}
