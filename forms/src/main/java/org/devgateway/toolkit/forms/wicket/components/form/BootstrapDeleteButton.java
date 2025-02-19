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
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.json.JSONObject;

/**
 * @author mpostelnicu
 *
 */
public abstract class BootstrapDeleteButton extends DgFmAjaxButton {

    private static final long serialVersionUID = 8306451874943978003L;

    /**
     * @param id
     * @param model
     */
    public BootstrapDeleteButton(final String id, final IModel<String> model) {
        super(id, model);
       add(new AttributeAppender("class", " btn-danger "));

    }

    public BootstrapDeleteButton(final String id) {
        super(id);
    }

    @Override
    protected abstract void onSubmit(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new AttributeAppender("onclick", new Model<>("window.onbeforeunload = null;"), " "));
        setDefaultFormProcessing(false);
        add(new IconBehavior(FontAwesome5IconType.trash_s));

        if (ComponentUtil.isPrintMode()) {
            setVisibilityAllowed(false);
        }
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
    }

    @Override
    protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {

        super.updateAjaxAttributes(attributes);
        AjaxCallListener ajaxCallListener = new AjaxCallListener();
        String message = getString("confirmDelete");
        ajaxCallListener.onPrecondition(String.format("return confirm(%s);", JSONObject.quote(message)));
        attributes.getAjaxCallListeners().add(ajaxCallListener);
    }

}
