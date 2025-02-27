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
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstrapcheckbox.BootstrapCheckBoxPicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstrapcheckbox.BootstrapCheckBoxPickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 *
 */
public class CheckBoxPickerBootstrapFormComponent
        extends GenericEnablingBootstrapFormComponent<Boolean, BootstrapCheckBoxPicker> {
    private static final long serialVersionUID = -4032850928243673675L;

    private Boolean isFloatedInput = false;

    private BootstrapCheckBoxPickerConfig config;

    public CheckBoxPickerBootstrapFormComponent(final String id, final IModel<String> labelModel,
            final IModel<Boolean> model) {
        super(id, labelModel, model);
    }

    /**
     * @param id
     * @param model
     */
    public CheckBoxPickerBootstrapFormComponent(final String id, final IModel<Boolean> model) {
        super(id, model);
    }

    public CheckBoxPickerBootstrapFormComponent(final String id) {
        super(id);
    }

    @Override
    protected BootstrapCheckBoxPicker inputField(final String id, final IModel<Boolean> model) {

        final BootstrapCheckBoxPickerConfig config = new BootstrapCheckBoxPickerConfig().withOnClass("btn-info")
                .withOffClass("btn-warning")
                .withOnIcon(FontAwesome5IconType.thumbs_up_r).withOffIcon(FontAwesome5IconType.thumbs_down_r)
                .withReverse(true);

        final BootstrapCheckBoxPicker checkBoxPicker = new BootstrapCheckBoxPicker("field", initFieldModel(), config);
        checkBoxPicker.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                CheckBoxPickerBootstrapFormComponent.this.onUpdate(target);
            }
        });
        checkBoxPicker.add(new AttributeModifier("style", "transform: scale(0.8);"));

        return checkBoxPicker;
    }

    @Override
    public String getUpdateEvent() {
        return "click";
    }

    @Override
    protected boolean boundComponentsVisibilityAllowed(final Boolean selectedValue) {
        return selectedValue;
    }
}
