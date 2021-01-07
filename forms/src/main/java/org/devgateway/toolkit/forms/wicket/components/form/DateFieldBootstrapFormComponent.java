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

import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.AbstractDateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.model.IModel;

import java.util.Date;

/**
 * @author mpostelnicu
 *
 */
public class DateFieldBootstrapFormComponent extends AbstractDateFieldBootstrapFormComponent<Date> {
    private static final long serialVersionUID = 4019013592708867249L;

    /**
     * @param id
     * @param labelModel
     * @param model
     */
    public DateFieldBootstrapFormComponent(final String id, final IModel<String> labelModel, final IModel<Date> model) {
        super(id, labelModel, model);
    }

    public DateFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    /**
     * @param id
     * @param model
     */
    public DateFieldBootstrapFormComponent(final String id, final IModel<Date> model) {
        super(id, model);
    }

    @Override
    protected DateTextFieldConfig newDateConfig() {
        return new DateTextFieldConfig();
    }

    @Override
    protected DateTextField newDateTextField(final String id, final AbstractDateTextFieldConfig config) {
        return new DateTextField(id, initFieldModel(), (DateTextFieldConfig) config) {
            @Override
            public boolean isRequired() {
                return isFmMandatory(super::isRequired);
            }
        };
    }
}
