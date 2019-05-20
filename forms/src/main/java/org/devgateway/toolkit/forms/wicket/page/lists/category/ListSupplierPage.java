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
package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.persistence.service.filterstate.category.SupplierFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditSupplierPage;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/suppliers")
public class ListSupplierPage extends AbstractListCategoryPage<Supplier> {

    @SpringBean
    protected SupplierService supplierService;

    public ListSupplierPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = supplierService;
        this.editPageClass = EditSupplierPage.class;
    }

    @Override
    protected void onInitialize() {
        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("code", ListSupplierPage.this)).getString()), "code",
                "code"));

        super.onInitialize();
    }

    @Override
    public JpaFilterState<Supplier> newFilterState() {
        return new SupplierFilterState();
    }
}
