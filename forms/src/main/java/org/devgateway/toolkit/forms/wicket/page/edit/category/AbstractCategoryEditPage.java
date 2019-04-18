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
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Category_;

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
        addUniqueNameValidator();
    }

    private void addUniqueNameValidator() {
        label.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueLabel"),
                jpaService::findOne,
                (o, v) -> (root, query, cb) -> cb.equal(cb.lower(root.get(Category_.label)), v.toLowerCase()),
                editForm.getModel()
        ));
    }

    protected void addCode() {
        final TextFieldBootstrapFormComponent<String> code = ComponentUtil.addTextField(editForm, "code");
        code.required();
        code.getField().add(VALIDATOR);

        code.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueCode"),
                jpaService::findOne,
                (o, v) -> (root, query, cb) -> cb.equal(cb.lower(root.get(Category_.code)), v.toLowerCase()),
                editForm.getModel()
        ));
    }
}
