package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author Octavian Ciubotaru
 */
public interface AjaxFormListener {

    void onSubmit(AjaxRequestTarget target);

    void onError(AjaxRequestTarget target);
}
