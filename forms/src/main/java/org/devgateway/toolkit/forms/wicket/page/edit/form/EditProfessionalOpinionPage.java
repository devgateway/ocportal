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
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

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

        final TextFieldBootstrapFormComponent<Double> recommendedAwardAmount = ComponentUtil.addDoubleField(editForm,
                "recommendedAwardAmount");
        recommendedAwardAmount.required();
        recommendedAwardAmount.getField().add(RangeValidator.minimum(0.0));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected ProfessionalOpinion newInstance() {
        final ProfessionalOpinion professionalOpinion = super.newInstance();
        professionalOpinion.setPurchaseRequisition(this.purchaseRequisition);
        this.purchaseRequisition.setProfessionalOpinion(professionalOpinion);

        return professionalOpinion;
    }

    @Override
    protected Class<? extends BasePage> pageAfterSubmitAndNext() {
        return EditAwardNotificationPage.class;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (editForm.getModelObject().getPurchaseRequisition().getAwardNotification() != null) {
            pp.set(WebConstants.PARAM_ID,
                    editForm.getModelObject().getPurchaseRequisition().getAwardNotification().getId());
        }

        return pp;
    }

    private List<Supplier> getSuppliersInTenderQuotation() {
        final TenderQuotationEvaluation tenderQuotationEvaluation = editForm.getModelObject()
                .getPurchaseRequisition().getTenderQuotationEvaluation();
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
