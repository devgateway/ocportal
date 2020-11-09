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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.FormSecurityUtil;
import org.devgateway.toolkit.forms.wicket.components.table.SelectMultiFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.user.EditUserPageElevated;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.RoleService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.PersonFilterState;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@AuthorizeInstantiation({SecurityConstants.Roles.ROLE_ADMIN, SecurityConstants.Roles.ROLE_PMC_ADMIN})
@MountPath(value = "/listusers")
public class ListUserPage extends AbstractListPage<Person> {

    private static final long serialVersionUID = 3529738250403399032L;

    @SpringBean
    private PersonService personService;

    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private RoleService roleService;

    public ListUserPage(final PageParameters pageParameters) {
        super(pageParameters);
        filterGoReset = true;
        this.jpaService = personService;
        this.editPageClass = EditUserPageElevated.class;
    }

    @Override
    protected void onInitialize() {
        columns.add(new TextFilteredBootstrapPropertyColumn<>(new StringResourceModel("name", this),
                "username", "username"));

        final List<Department> departments = departmentService.findAll();
        columns.add(new SelectMultiFilteredBootstrapPropertyColumn<>(new StringResourceModel("departments", this),
                "departments", new ListModel<>(departments), dataTable));

        List<Role> roles = null;
        if (FormSecurityUtil.isCurrentUserAdmin()) {
            roles = roleService.findAll();
        } else {
            if (FormSecurityUtil.isCurrentUserPmcAdmin()) {
                roles = roleService.findByAuthorityIn(SecurityConstants.Roles.PMC_ROLES);
            }
        }
        columns.add(new SelectMultiFilteredBootstrapPropertyColumn<>(new StringResourceModel("roles", this),
                "roles", new ListModel<>(roles), dataTable,
                !FormSecurityUtil.isCurrentUserPmcAdmin()));

        super.onInitialize();
        // enable excel download
        excelForm.setVisibilityAllowed(true);
    }

    @Override
    public JpaFilterState<Person> newFilterState() {
        PersonFilterState personFilterState = new PersonFilterState();
        if (FormSecurityUtil.isCurrentUserPmcAdmin()) {
            personFilterState.getRoles().addAll(roleService.findByAuthorityIn(SecurityConstants.Roles.PMC_ROLES));
        }
        return personFilterState;
    }
}