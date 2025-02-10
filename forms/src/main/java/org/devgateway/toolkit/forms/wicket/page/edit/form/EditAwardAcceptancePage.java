package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.AwardAcceptanceItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.ProcurementRoleAssignable;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
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
public class EditAwardAcceptancePage extends EditAbstractTenderReqClientEntityPage<AwardAcceptance>
        implements ProcurementRoleAssignable {
    @SpringBean
    protected AwardAcceptanceService awardAcceptanceService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    private GenericSleepFormComponent supplierID;

    public EditAwardAcceptancePage() {
        this(new PageParameters());
    }

    public EditAwardAcceptancePage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = awardAcceptanceService;
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("awardAcceptanceForm");
        super.onInitialize();

        AwardAcceptanceItemPanel items = new AwardAcceptanceItemPanel("items");
        editForm.add(items);
    }

    @Override
    protected AwardAcceptance newInstance() {
        final AwardAcceptance awardAcceptance = super.newInstance();
        awardAcceptance.setTenderProcess(sessionMetadataService.getSessionTenderProcess());

        return awardAcceptance;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (!editForm.getModelObject().hasAccepted()
                && !ObjectUtils.isEmpty(editForm.getModelObject().getTenderProcess().getSingleProfessionalOpinion())) {
            pp.set(
                    WebConstants.PARAM_ID,
                    PersistenceUtil.getNext(
                            editForm.getModelObject().getTenderProcess().getProfessionalOpinion()).getId()
            );
            return pp;
        }
        if (!ObjectUtils.isEmpty(editForm.getModelObject().getTenderProcess().getContract())) {
            pp.set(WebConstants.PARAM_ID,
                    PersistenceUtil.getNext(
                            editForm.getModelObject().getTenderProcess().getContract()).getId());
        }

        return pp;
    }


}
