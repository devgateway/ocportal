package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.AwardNotificationFilterState;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/awardNotifications")
public class ListAwardNotificationPage extends ListAbstractTenderProcessMakueniEntity<AwardNotification> {
    @SpringBean
    protected AwardNotificationService awardNotificationService;

    public ListAwardNotificationPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = awardNotificationService;
        this.editPageClass = EditAwardNotificationPage.class;
    }

    @Override
    protected void onInitialize() {
        addTenderTitleColumn();
        addFileDownloadColumn();
        addAwardeeColumn();
        super.onInitialize();
    }


    @Override
    public JpaFilterState<AwardNotification> newFilterState() {
        return new AwardNotificationFilterState();
    }
}
