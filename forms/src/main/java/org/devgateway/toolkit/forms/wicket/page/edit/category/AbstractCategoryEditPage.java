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
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.categories.Category;

/**
 * @author mpostelnicu
 */

public abstract class AbstractCategoryEditPage<T extends Category> extends AbstractEditPage<T> {
    private static final long serialVersionUID = 6571076983713857766L;

    protected static final StringValidator VALIDATOR =
            WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT;

    protected TextFieldBootstrapFormComponent<String> label;

    public AbstractCategoryEditPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // just replace the page title with the name of the class
        // instead of having .properties files only for the page title
        addOrReplace(new Label("pageTitle",
                StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                        this.getClass().getSimpleName().replaceAll("Page", "")), ' ')));

        label = ComponentUtil.addTextField(editForm, "label");
        label.required();
        label.getField().add(VALIDATOR);
    }
}
