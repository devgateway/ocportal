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
package org.devgateway.toolkit.forms.wicket.page.overview;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.devgateway.toolkit.forms.wicket.page.DataEntryBasePage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@MountPath("/statusOverview")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class StatusOverviewPage extends DataEntryBasePage {
    /**
     * @param parameters
     */
    public StatusOverviewPage(final PageParameters parameters) {
        super(parameters);       
    }
    
    @Override
    protected void onInitialize() {
        super.onInitialize();
        final SideBar sideBar = new SideBar("sideBar");         
        add(sideBar);        
        final StatusOverviewMainPanel departmentOverview = new StatusOverviewMainPanel("departmentOverview");        
        add(departmentOverview);
        
        final Image logo = new Image("logo", new PackageResourceReference(BaseStyles.class,
                "assets/img/logo.png"));        
        add(logo);        
    }  
}
