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

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;

/**
 * @author mpostelnicu
 *
 * A {@link TextAreaFieldBootstrapFormComponent} that has TextArea{@link #isRequired()} exposed
 *
 */
public abstract class OptionallyRequiredTextAreaFieldComponent<TYPE> extends TextAreaFieldBootstrapFormComponent<TYPE> {
    private StringValidator validator = WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXTAREA;

    private static final long serialVersionUID = 1L;

    public OptionallyRequiredTextAreaFieldComponent(final String id, final IModel<String> labelModel,
                                                    final IModel<TYPE> model) {
        super(id, labelModel, model);
    }

    public OptionallyRequiredTextAreaFieldComponent(final String id, final IModel<String> labelModel) {
        super(id, labelModel, null);
    }

    /**
     * @param id
     */
    public OptionallyRequiredTextAreaFieldComponent(final String id) {
        super(id);
    }

    public abstract boolean isRequired();

    @Override
    protected TextArea<TYPE> inputField(final String id, final IModel<TYPE> model) {
        TextArea<TYPE> textArea = new TextArea<TYPE>(id, initFieldModel()) {
            @Override
            public boolean isRequired() {
                return OptionallyRequiredTextAreaFieldComponent.this.isRequired();
            }
        };
        return textArea;
    }

}
