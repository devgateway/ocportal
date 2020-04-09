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
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditMEStaffPage;
import org.devgateway.toolkit.persistence.dao.categories.MEStaff;
import org.devgateway.toolkit.persistence.service.category.MEStaffService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListMEStaffPage extends AbstractListCategoryPage<MEStaff> {

    @SpringBean
    protected MEStaffService staffService;

    public ListMEStaffPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = staffService;
        this.editPageClass = EditMEStaffPage.class;
    }

}
