package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.AwardNotificationItemPanel;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditAwardNotificationPage extends EditAbstractTenderReqMakueniEntity<AwardNotification> {
    @SpringBean
    protected AwardNotificationService awardNotificationService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    public EditAwardNotificationPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = awardNotificationService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        AwardNotificationItemPanel items = new AwardNotificationItemPanel("items");
        editForm.add(items);
    }

    @Override
    protected AbstractTenderProcessMakueniEntity getNextForm() {
        return editForm.getModelObject().getTenderProcess().getSingleAwardAcceptance();
    }

    @Override
    protected AwardNotification newInstance() {
        final AwardNotification awardNotification = super.newInstance();
        awardNotification.setTenderProcess(sessionMetadataService.getSessionTenderProcess());

        return awardNotification;
    }

    @Override
    protected void beforeSaveEntity(final AwardNotification awardNotification) {
        super.beforeSaveEntity(awardNotification);

        final TenderProcess tenderProcess = awardNotification.getTenderProcess();
        tenderProcess.addAwardNotification(awardNotification);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected void beforeDeleteEntity(final AwardNotification awardNotification) {
        super.beforeDeleteEntity(awardNotification);

        final TenderProcess tenderProcess = awardNotification.getTenderProcess();
        tenderProcess.removeAwardNotification(awardNotification);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected Class<? extends BasePage> pageAfterSubmitAndNext() {
        return EditAwardAcceptancePage.class;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (!ObjectUtils.isEmpty(editForm.getModelObject().getTenderProcess().getAwardAcceptance())) {
            pp.set(WebConstants.PARAM_ID,
                    PersistenceUtil.getNext(
                            editForm.getModelObject().getTenderProcess().getAwardAcceptance()).getId());
        }

        return pp;
    }



}
