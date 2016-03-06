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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.core.util.Attributes;

public class PasswordFieldBootstrapFormComponent extends GenericBootstrapFormComponent<String, PasswordTextField> {
	private static final long serialVersionUID = -2865390099361839324L;

    private Boolean isFloatedInput = false;

	/**
	 * @param id
	 * @param labelModel
	 * @param model
	 */
	public PasswordFieldBootstrapFormComponent(String id, IModel<String> labelModel, IModel<String> model) {
		super(id, labelModel, model);
	}

	/**
	 * @param id
	 * @param model
	 */
	public PasswordFieldBootstrapFormComponent(String id, IModel<String> model) {
		super(id, model);
	}

	/**
	 * @param id
	 */
	public PasswordFieldBootstrapFormComponent(String id) {
		super(id);
	}

	@Override
	protected PasswordTextField inputField(String id, IModel<String> model){
		return (PasswordTextField) new PasswordTextField(id,initFieldModel());
	}

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        if(isFloatedInput) {
            Attributes.addClass(tag, "floated-input");
        }
    }

    public Boolean getIsFloatedInput() {
        return isFloatedInput;
    }

    public void setIsFloatedInput(Boolean isFloatedInput) {
        this.isFloatedInput = isFloatedInput;
    }
}



