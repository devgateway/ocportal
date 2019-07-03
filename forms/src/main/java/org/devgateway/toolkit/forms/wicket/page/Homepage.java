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
package org.devgateway.toolkit.forms.wicket.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.web.db.ImportPostgresToMongo;
import org.devgateway.toolkit.forms.service.MakueniToOCDSConversionService;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.HomeStyles;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * @author mpostelnicu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class Homepage extends BasePage {
    @SpringBean
    private ImportPostgresToMongo importPostgresToMongo;

    public Homepage(final PageParameters parameters) {
        super(parameters);
    }

    @SpringBean
    private MakueniToOCDSConversionService ocdsConversionService;

    @SpringBean
    PurchaseRequisitionService purchaseRequisitionService;

    @SpringBean
    private ObjectMapper mapper;


    @Override
    protected void onInitialize() {
        super.onInitialize();

        try {
            importPostgresToMongo.test();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final Link<Void> dataEntryLink = new Link<Void>("dataEntryLink") {
            @Override
            public void onClick() {
                // clear all session data before going to the dashboard
                SessionMetadataService.clearSessionData();
                setResponsePage(StatusOverviewPage.class);
            }
        };
        add(dataEntryLink);

        final Link<Void> dataExport = new Link<Void>("dataExport") {
            @Override
            public void onClick() {
                setResponsePage(DataExportPage.class);
            }
        };
        add(dataExport);


//        Release release = ocdsConversionService.createRelease(purchaseRequisitionService.findById(16200L).get());
//        try {
//            System.out.println(mapper.writeValueAsString(release));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // Load Home Page Styles
        response.render(CssHeaderItem.forReference(HomeStyles.INSTANCE));
    }
}
