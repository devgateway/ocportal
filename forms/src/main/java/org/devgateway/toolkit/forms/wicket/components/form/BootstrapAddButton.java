/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;

/**
 * @author mpostelnicu
 *
 */
public abstract class BootstrapAddButton extends DgFmAjaxButton {

    private static final long serialVersionUID = 8306451874943978003L;

    protected BootstrapAddButton(final String id) {
        super(id);
    }
    /**
     * @param id
     * @param model
     */
    protected BootstrapAddButton(final String id, final IModel<String> model) {
        super(id, model);
        add(new IconBehavior(FontAwesome5IconType.save_r));

        setDefaultFormProcessing(false);
        add(new IconBehavior(FontAwesome5IconType.plus_s));

        add(new IconBehavior(FontAwesome5IconType.plus_s));
        add(new AttributeModifier("size", Size.Medium));

        setLabel(model);


        setOutputMarkupPlaceholderTag(true);
    }


    @Override
    protected abstract void onSubmit(AjaxRequestTarget target);

    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (ComponentUtil.isPrintMode()) {
            setVisibilityAllowed(false);
        }
    }

}
