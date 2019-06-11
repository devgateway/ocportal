package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.BidPanel;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/tenderQuotationEvaluation")
public class EditTenderQuotationEvaluationPage extends EditAbstractPurchaseReqMakueniEntity<TenderQuotationEvaluation> {

    @SpringBean
    protected TenderQuotationEvaluationService tenderQuotationEvaluationService;

    @SpringBean
    protected PurchaseRequisitionService purchaseRequisitionService;

    public EditTenderQuotationEvaluationPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderQuotationEvaluationService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addDateField(editForm, "closingDate").required();
        ComponentUtil.addIntegerTextField(editForm, "numberOfBids").required()
                .getField().add(RangeValidator.minimum(0));
        editForm.add(new BidPanel("bids"));

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected TenderQuotationEvaluation newInstance() {
        final TenderQuotationEvaluation tenderQuotationEvaluation = super.newInstance();
        tenderQuotationEvaluation.setPurchaseRequisition(sessionMetadataService.getSessionPurchaseRequisition());

        return tenderQuotationEvaluation;
    }

    @Override
    protected void beforeSaveEntity(final TenderQuotationEvaluation tenderQuotationEvaluation) {
        super.beforeSaveEntity(tenderQuotationEvaluation);

        final PurchaseRequisition purchaseRequisition = tenderQuotationEvaluation.getPurchaseRequisition();
        purchaseRequisition.addTenderQuotationEvaluation(tenderQuotationEvaluation);
        purchaseRequisitionService.save(purchaseRequisition);
    }

    @Override
    protected void beforeDeleteEntity(final TenderQuotationEvaluation tenderQuotationEvaluation) {
        super.beforeDeleteEntity(tenderQuotationEvaluation);

        final PurchaseRequisition purchaseRequisition = tenderQuotationEvaluation.getPurchaseRequisition();
        purchaseRequisition.removeTenderQuotationEvaluation(tenderQuotationEvaluation);
        purchaseRequisitionService.save(purchaseRequisition);
    }

    @Override
    protected Class<? extends BasePage> pageAfterSubmitAndNext() {
        return EditProfessionalOpinionPage.class;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (!ObjectUtils.isEmpty(editForm.getModelObject().getPurchaseRequisition().getProfessionalOpinion())) {
            pp.set(WebConstants.PARAM_ID,
                    PersistenceUtil.getNext(editForm.getModelObject().getPurchaseRequisition()
                            .getProfessionalOpinion()).getId());
        }

        return pp;
    }
}
