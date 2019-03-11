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
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditFiscalYearPage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/fiscalYears")
public class ListFiscalYearPage extends AbstractListPage<FiscalYear> {

    @SpringBean
    protected FiscalYearService service;

    public ListFiscalYearPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = service;
        this.editPageClass = EditFiscalYearPage.class;
        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("name", ListFiscalYearPage.this)).getString()), "name",
                "name"
        ));
        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("startDate", ListFiscalYearPage.this)).getString()), "startDate",
                "startDate"
        ));

        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("endDate", ListFiscalYearPage.this)).getString()), "endDate",
                "endDate"
        ));

    }
}
