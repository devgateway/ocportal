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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditPMCStaffPage;
import org.devgateway.toolkit.persistence.dao.categories.PMCStaff;
import org.devgateway.toolkit.persistence.service.category.PMCStaffService;
import org.wicketstuff.annotation.mount.MountPath;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_ADMIN;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_VALIDATOR;

@AuthorizeInstantiation({ROLE_PMC_ADMIN, ROLE_PMC_VALIDATOR, ROLE_IMPLEMENTATION_USER })
@MountPath
public class ListPMCStaffPage extends AbstractListCategoryPage<PMCStaff> {

    @SpringBean
    protected PMCStaffService staffService;

    public ListPMCStaffPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = staffService;
        this.editPageClass = EditPMCStaffPage.class;
    }
}
