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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.EditFiscalYearBudgetPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.category.FiscalYearBudgetFilterState;
import org.devgateway.toolkit.persistence.service.form.FiscalYearBudgetService;
import org.devgateway.toolkit.web.security.SecurityConstants;

import java.util.List;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
public class ListFiscalYearBudgetPage extends AbstractListPage<FiscalYearBudget> {

    @SpringBean
    protected FiscalYearBudgetService fiscalYearBudgetService;

    @SpringBean
    protected DepartmentService departmentService;

    @SpringBean
    protected FiscalYearService fiscalYearService;

    public ListFiscalYearBudgetPage(final PageParameters pageParameters) {
        super(pageParameters);

        filterGoReset = true;
        this.jpaService = fiscalYearBudgetService;
        this.editPageClass = EditFiscalYearBudgetPage.class;
        this.departments = departmentService.findAll();
        this.fiscalYears = fiscalYearService.findAll();
    }

    protected final List<Department> departments;

    protected final List<FiscalYear> fiscalYears;

    @Override
    protected void onInitialize() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Department"),
                "department", "department", new ListModel<>(departments), dataTable
        ));

        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Fiscal Year"),
                "fiscalYear", "fiscalYear", new ListModel<>(fiscalYears), dataTable
        ));

        super.onInitialize();
        editPageLink.setVisibilityAllowed(false);
        topEditPageLink.setVisibilityAllowed(false);
    }

    @Override
    public JpaFilterState<FiscalYearBudget> newFilterState() {
        return new FiscalYearBudgetFilterState();
    }
}