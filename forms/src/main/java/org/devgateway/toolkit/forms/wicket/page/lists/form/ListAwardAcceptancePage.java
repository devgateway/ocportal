package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.AwardAcceptanceFilterState;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/awardAcceptanceList")
public class ListAwardAcceptancePage extends ListAbstractTenderProcessMakueniEntity<AwardAcceptance> {
    @SpringBean
    protected AwardAcceptanceService awardAcceptanceService;

    public ListAwardAcceptancePage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = awardAcceptanceService;
        this.editPageClass = EditAwardAcceptancePage.class;
    }

    @Override
    protected void onInitialize() {
        addTenderTitleColumn();
        addFileDownloadColumn();
        addAwardeeColumn();
        super.onInitialize();

    }


    @Override
    public JpaFilterState<AwardAcceptance> newFilterState() {
        return new AwardAcceptanceFilterState();
    }
}
