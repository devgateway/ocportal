package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-24
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/professionalOpinion")
public class EditProfessionalOpinionPage extends EditAbstractPurchaseReqMakueniEntity<ProfessionalOpinion> {
    @SpringBean
    protected ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    protected PurchaseRequisitionService purchaseRequisitionService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    public EditProfessionalOpinionPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = professionalOpinionService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>("awardee",
                new GenericChoiceProvider<>(getSuppliersInTenderQuotation()));
        awardeeSelector.required();
        editForm.add(awardeeSelector);

        ComponentUtil.addDateField(editForm, "professionalOpinionDate").required();

        final TextFieldBootstrapFormComponent<BigDecimal> recommendedAwardAmount =
                ComponentUtil.addBigDecimalField(editForm, "recommendedAwardAmount");
        recommendedAwardAmount.required();
        recommendedAwardAmount.getField().add(RangeValidator.minimum(BigDecimal.ZERO));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected ProfessionalOpinion newInstance() {
        final ProfessionalOpinion professionalOpinion = super.newInstance();
        professionalOpinion.setPurchaseRequisition(sessionMetadataService.getSessionPurchaseRequisition());

        return professionalOpinion;
    }

    @Override
    protected void beforeSaveEntity(final ProfessionalOpinion professionalOpinion) {
        super.beforeSaveEntity(professionalOpinion);

        final PurchaseRequisition purchaseRequisition = professionalOpinion.getPurchaseRequisition();
        purchaseRequisition.addProfessionalOpinion(professionalOpinion);
        purchaseRequisitionService.save(purchaseRequisition);
    }

    @Override
    protected void beforeDeleteEntity(final ProfessionalOpinion professionalOpinion) {
        super.beforeDeleteEntity(professionalOpinion);

        final PurchaseRequisition purchaseRequisition = professionalOpinion.getPurchaseRequisition();
        purchaseRequisition.removeProfessionalOpinion(professionalOpinion);
        purchaseRequisitionService.save(purchaseRequisition);
    }

    @Override
    protected Class<? extends BasePage> pageAfterSubmitAndNext() {
        return EditAwardNotificationPage.class;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (!ObjectUtils.isEmpty(editForm.getModelObject().getPurchaseRequisition().getAwardNotification())) {
            pp.set(WebConstants.PARAM_ID,
                    PersistenceUtil.getNext(
                            editForm.getModelObject().getPurchaseRequisition().getAwardNotification()).getId());
        }

        return pp;
    }

    private List<Supplier> getSuppliersInTenderQuotation() {
        final PurchaseRequisition purchaseRequisition = editForm.getModelObject().getPurchaseRequisition();
        final TenderQuotationEvaluation tenderQuotationEvaluation =
                PersistenceUtil.getNext(purchaseRequisition.getTenderQuotationEvaluation());
        final List<Supplier> suppliers = new ArrayList<>();
        if (tenderQuotationEvaluation != null && !tenderQuotationEvaluation.getBids().isEmpty()) {
            for (Bid bid : tenderQuotationEvaluation.getBids()) {
                if (bid.getSupplier() != null) {
                    suppliers.add(bid.getSupplier());
                }
            }
        }

        return suppliers;
    }
}
