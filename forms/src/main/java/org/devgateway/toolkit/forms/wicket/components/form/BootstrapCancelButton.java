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
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;

/**
 * @author mpostelnicu
 *
 */
public abstract class BootstrapCancelButton extends DgFmAjaxButton {

    private static final long serialVersionUID = -5748825183253028913L;

    /**
     * @param id
     * @param model
     */
    public BootstrapCancelButton(final String id, final IModel<String> model) {
        super(id, model);
        add(new AttributeAppender("class", Buttons.Type.Default));

        setDefaultFormProcessing(false);
        add(new IconBehavior(FontAwesome5IconType.ban_s));
    }

    @Override
    protected abstract void onSubmit(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (ComponentUtil.isPrintMode()) {
            setVisibilityAllowed(false);
        }
    }
}
