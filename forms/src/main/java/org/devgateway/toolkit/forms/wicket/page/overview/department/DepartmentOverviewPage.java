/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.overview.department;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.forms.wicket.page.overview.SideBar;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.forms.wicket.styles.OverviewStyles;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@MountPath("/departmentOverview")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class DepartmentOverviewPage extends DataEntryBasePage {
    /**
     * @param parameters
     */
    private Long departmentId = null;
    private Long fiscalYearId = null;

    public DepartmentOverviewPage(final PageParameters parameters) {
        super(parameters);
        if (!parameters.get(WebConstants.PARAM_DEPARTMENT_ID).isNull()) {
            this.departmentId = parameters.get(WebConstants.PARAM_DEPARTMENT_ID).toLong();
        }

        if (!parameters.get(WebConstants.PARAM_FISCAL_YEAR_ID).isNull()) {
            this.fiscalYearId = parameters.get(WebConstants.PARAM_FISCAL_YEAR_ID).toLong();
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final SideBar sideBar = new SideBar("sideBar");
        add(sideBar);

        final DepartmentOverviewMainPanel mainPanel = new DepartmentOverviewMainPanel("mainPanel", this.departmentId,
                fiscalYearId);
        add(mainPanel);

        final Image logo = new Image("logo", new PackageResourceReference(BaseStyles.class, "assets/img/logo.png"));
        add(logo);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // Load Styles.
        response.render(CssHeaderItem.forReference(OverviewStyles.INSTANCE));

    }
}
