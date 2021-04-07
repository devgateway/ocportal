package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.AwardNotificationItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.ProcurementRoleAssignable;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditAwardNotificationPage extends EditAbstractTenderReqMakueniEntityPage<AwardNotification>
        implements ProcurementRoleAssignable {
    @SpringBean
    protected AwardNotificationService awardNotificationService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    public EditAwardNotificationPage() {
        this(new PageParameters());
    }


    public EditAwardNotificationPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = awardNotificationService;
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("awardNotificationForm");
        super.onInitialize();

        AwardNotificationItemPanel items = new AwardNotificationItemPanel("items");
        editForm.add(items);
    }

    @Override
    protected AwardNotification newInstance() {
        final AwardNotification awardNotification = super.newInstance();
        awardNotification.setTenderProcess(sessionMetadataService.getSessionTenderProcess());

        return awardNotification;
    }
}
