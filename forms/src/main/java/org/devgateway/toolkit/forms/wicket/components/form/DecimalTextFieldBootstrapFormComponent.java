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

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.DoubleNumberTextField;

/**
 * @author mpostelnicu
 * A form component dedicated to decimals that require large (above 3 which is wicket default) number of digits.
 *
 */
public class DecimalTextFieldBootstrapFormComponent extends GenericBootstrapFormComponent<Double, DoubleNumberTextField> {

    private static final StringValidator VALIDATOR =
            WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXT;

    public DecimalTextFieldBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                                  final IModel<Double> model) {
        super(id, labelModel, model);
    }

    public DecimalTextFieldBootstrapFormComponent(final String id, final IModel<Double> model) {
        super(id, model);
    }

    /**
     * @param id
     */
    public DecimalTextFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    @Override
    protected DoubleNumberTextField inputField(final String id, final IModel<Double> model) {
        return new DoubleNumberTextField(id, initFieldModel()) {
            @Override
            public boolean isRequired() {
                return isFmMandatory(super::isRequired);
            }
        };
    }

    public DecimalTextFieldBootstrapFormComponent autoCompleteOff() {
        field.add(AttributeAppender.append("autocomplete", "new-password"));
        return this;
    }
}
