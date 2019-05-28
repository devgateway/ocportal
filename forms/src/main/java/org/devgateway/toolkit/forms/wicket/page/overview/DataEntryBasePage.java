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
package org.devgateway.toolkit.forms.wicket.page.overview;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.styles.OverviewStyles;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataEntryBasePage extends BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(DataEntryBasePage.class);

    protected SideBar sideBar;

    @SpringBean
    protected SessionMetadataService sessionMetadataService;

    @SpringBean
    protected FiscalYearService fiscalYearService;

    public DataEntryBasePage(final PageParameters parameters) {
        super(parameters);

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        pageTitle.setVisibilityAllowed(false);

        sideBar = new SideBar("sideBar");
        add(sideBar);
    }

    @Override
    public Boolean fluidContainer() {
        return true;
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // Load Styles.
        response.render(CssHeaderItem.forReference(OverviewStyles.INSTANCE));
    }
}
