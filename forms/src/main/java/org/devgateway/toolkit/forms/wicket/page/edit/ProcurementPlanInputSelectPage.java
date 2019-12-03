package org.devgateway.toolkit.forms.wicket.page.edit;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.ImportProcurementPlanItemsPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@MountPath("/procurementPlanInputSelect")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class ProcurementPlanInputSelectPage extends BasePage {
    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public ProcurementPlanInputSelectPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final BookmarkablePageLink<Void> newProcurementPlanButton = new BookmarkablePageLink<Void>(
                "newProcurementPlan", EditProcurementPlanPage.class);
        add(newProcurementPlanButton);


        final BookmarkablePageLink<Void> importProcurementPlan = new BookmarkablePageLink<>(
                "importProcurementPlan", ImportProcurementPlanItemsPage.class);
        add(importProcurementPlan);
    }
}
