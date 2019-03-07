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
package org.devgateway.toolkit.forms.wicket.page.lists;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditDepartmentPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/departments")
public class ListDepartmentPage extends AbstractListPage<Department> {

    @SpringBean
    protected DepartmentService service;

    public ListDepartmentPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = service;
        this.editPageClass = EditDepartmentPage.class;
        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("name", ListDepartmentPage.this)).getString()), "label",
                "label"));
        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("code", ListDepartmentPage.this)).getString()), "code",
                "code"));
    }
}
