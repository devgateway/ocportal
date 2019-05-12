package org.devgateway.toolkit.forms.wicket.page.overview.department;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderQuotationEvaluationPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvalutionService;
import org.devgateway.toolkit.persistence.service.form.TenderService;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public class TenderItem extends Panel {
    private static final long serialVersionUID = -2887946738171526100L;
    private Boolean expanded = false;
    private PurchaseRequisition purchaseRequisition;

    @SpringBean
    private TenderService tenderService;

    @SpringBean
    private TenderQuotationEvalutionService tenderQuotationEvalutionService;

    @SpringBean
    private ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    private AwardNotificationService awardNotificationService;

    @SpringBean
    private AwardAcceptanceService awardAcceptanceService;

    @SpringBean
    private ContractService contractService;
    private Tender tender;
    private TenderQuotationEvaluation tenderQuotationEvaluation;
    private ProfessionalOpinion professionalOpinion;
    private AwardNotification awardNotification;
    private AwardAcceptance awardAcceptance;
    private Contract contract;

    public TenderItem(final String id, final PurchaseRequisition purchaseRequisition) {
        super(id);
        this.purchaseRequisition = purchaseRequisition;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        tender = tenderService.findByPurchaseRequisition(purchaseRequisition);
        tenderQuotationEvaluation = tenderQuotationEvalutionService.findByPurchaseRequisition(purchaseRequisition);
        professionalOpinion = professionalOpinionService.findByPurchaseRequisition(purchaseRequisition);

        awardNotification = awardNotificationService.findByPurchaseRequisition(purchaseRequisition);
        awardAcceptance = awardAcceptanceService.findByPurchaseRequisition(purchaseRequisition);
        contract = contractService.findByPurchaseRequisition(purchaseRequisition);

        addGroupHeader();
        addPurchaseRequisitionSection();
        addTenderDocumentSection();
        addTenderEvaluationSection();
        addProfessionalOpinionSection();
        addAwardNotificationSection();
        addAwardAcceptanceSection();
        addContractSection();
    }

    private void addGroupHeader() {
        final TransparentWebMarkupContainer hideableContainer = new TransparentWebMarkupContainer("projectsWrapper");
        hideableContainer.setOutputMarkupId(true);
        hideableContainer.setOutputMarkupPlaceholderTag(true);
        add(hideableContainer);

        final AjaxLink<Void> header = new AjaxLink<Void>("header") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                if (expanded) {
                    expanded = false;
                    target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('hide')");
                } else {
                    expanded = true;
                    target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('show')");
                }

            }
        };

        add(header);
    }

    private void addPurchaseRequisitionSection() {
        add(new Label("requisitionTitle", Model.of(purchaseRequisition.getTitle())));
        add(new Label("requisitionDate", Model.of(purchaseRequisition.getRequestApprovalDate())));
        add(new Label("requisitionAmount", Model.of(purchaseRequisition.getAmount())));
        Label statusLabel = new Label("requisitionStatus");
        statusLabel.add(AttributeAppender.append("class", purchaseRequisition.getStatus().toLowerCase()));
        add(statusLabel);

        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, purchaseRequisition.getId());
        final BootstrapBookmarkablePageLink<Void> purchaseRequisitionEdit = new BootstrapBookmarkablePageLink<Void>(
                "requisitionEdit", EditPurchaseRequisitionPage.class, pageParameters, Buttons.Type.Success);
        add(purchaseRequisitionEdit);

    }

    private void addTenderDocumentSection() {
        if (tender != null) {
            add(new Label("tenderTitle", Model.of(tender.getTenderTitle())));
            add(new Label("tenderId", Model.of(tender.getTenderNumber())));
            add(new Label("amount", Model.of(tender.getTotalAmount())));
            addStatus("tenderStatus", tender.getStatus().toLowerCase());
            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, tender.getId());
            createLink("editTender", pageParameters, "btn-edit", EditTenderPage.class, true);
        } else {
            add(new Label("tenderTitle", Model.of("")));
            add(new Label("tenderId", Model.of("")));
            add(new Label("amount", Model.of("")));
            addStatus("tenderStatus", DBConstants.Status.NOT_STARTED.toLowerCase());
            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            pageParameters.set(WebConstants.PARAM_PURCHASE_REQUISITION_ID, purchaseRequisition.getId());
            createLink("editTender", pageParameters, "btn-add no-text", EditTenderPage.class, true);
        }

    }

    private void createLink(final String id, final PageParameters pageParameters, final String cssClasses,
            final Class clazz, final boolean enabled) {
        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<Void>(id, clazz,
                pageParameters, Buttons.Type.Success);
        button.add(AttributeAppender.append("class", cssClasses));
        button.setEnabled(enabled);
        add(button);
    }

    private void addStatus(final String id, final String status) {
        Label statusLabel = new Label(id);
        statusLabel.add(AttributeAppender.append("class", status));
        add(statusLabel);
    }

    private void addTenderEvaluationSection() {
        if (tenderQuotationEvaluation != null) {
            add(new Label("tenderOpeningTitle", Model.of(tender.getTenderTitle())));
            add(new Label("tenderOpeningID", Model.of(tender.getTenderNumber())));
            add(new Label("tenderOpeningAmount", Model.of(0)));
            addStatus("tenderOpeningStatus", tenderQuotationEvaluation.getStatus().toLowerCase());
            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, tenderQuotationEvaluation.getId());
            createLink("editTenderOpening", pageParameters, "btn-edit", EditTenderQuotationEvaluationPage.class,
                    tender != null);
        } else {
            add(new Label("tenderOpeningTitle", Model.of("")));
            add(new Label("tenderOpeningID", Model.of("")));
            add(new Label("tenderOpeningAmount", Model.of("")));
            addStatus("tenderOpeningStatus", DBConstants.Status.NOT_STARTED.toLowerCase());
            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            if (tender != null) {
                pageParameters.set(WebConstants.PARAM_TENDER_ID, tender.getId());
            }
            createLink("editTenderOpening", pageParameters, "btn-add no-text", EditTenderQuotationEvaluationPage.class,
                    tender != null);
        }

    }

    private void addProfessionalOpinionSection() {
        if (professionalOpinion != null) {
            add(new Label("professionalOpinionSupplier", Model.of(professionalOpinion.getAwardee())));
            add(new Label("professionalOpinionAmount", Model.of(professionalOpinion.getRecommendedAwardAmount())));
            addStatus("professionalOpinionStatus", professionalOpinion.getStatus().toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, professionalOpinion.getId());
            createLink("editProfessionalOpinion", pageParameters, "btn-edit", EditProfessionalOpinionPage.class,
                    tenderQuotationEvaluation != null);
        } else {
            add(new Label("professionalOpinionSupplier", Model.of("")));
            add(new Label("professionalOpinionAmount", Model.of("")));
            addStatus("professionalOpinionStatus", DBConstants.Status.NOT_STARTED.toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            if (tenderQuotationEvaluation != null) {
                pageParameters.set(WebConstants.PARAM_TENDER_OPENING_ID, tenderQuotationEvaluation.getId());
            }
            createLink("editProfessionalOpinion", pageParameters, "btn-add no-text", EditProfessionalOpinionPage.class,
                    tenderQuotationEvaluation != null);
        }
    }

    private void addAwardNotificationSection() {
        if (awardNotification != null) {
            add(new Label("awardNotificationSupplier", Model.of(awardNotification.getAwardee())));
            add(new Label("awardNotificationTenderId", Model.of(tender.getTenderNumber())));
            addStatus("awardNotificationStatus", awardNotification.getStatus().toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, awardNotification.getId());
            createLink("editAwardNotification", pageParameters, "btn-edit", EditAwardNotificationPage.class,
                    professionalOpinion != null);
        } else {
            add(new Label("awardNotificationSupplier", Model.of("")));
            add(new Label("awardNotificationTenderId", Model.of("")));
            addStatus("awardNotificationStatus", DBConstants.Status.NOT_STARTED.toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            if (tenderQuotationEvaluation != null) {
                pageParameters.set(WebConstants.PARAM_TENDER_OPENING_ID, tenderQuotationEvaluation.getId());
            }
            createLink("editAwardNotification", pageParameters, "btn-add no-text", EditAwardNotificationPage.class,
                    professionalOpinion != null);
        }

    }

    private void addAwardAcceptanceSection() {
        if (awardAcceptance != null) {
            add(new Label("awardAcceptanceSupplier", Model.of(awardAcceptance.getAwardee())));
            add(new Label("awardAcceptanceTenderId", Model.of(tender.getTenderNumber())));
            addStatus("awardAcceptanceStatus", awardAcceptance.getStatus().toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, awardAcceptance.getId());
            createLink("editAwardAcceptance", pageParameters, "btn-edit", EditAwardAcceptancePage.class,
                    awardNotification != null);
        } else {
            add(new Label("awardAcceptanceSupplier", Model.of("")));
            add(new Label("awardAcceptanceTenderId", Model.of("")));
            addStatus("awardAcceptanceStatus", DBConstants.Status.NOT_STARTED.toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            if (tenderQuotationEvaluation != null) {
                pageParameters.set(WebConstants.PARAM_TENDER_OPENING_ID, tenderQuotationEvaluation.getId());
            }
            createLink("editAwardAcceptance", pageParameters, "btn-add no-text", EditAwardAcceptancePage.class,
                    awardNotification != null);
        }
    }

    private void addContractSection() {
        if (contract != null) {
            add(new Label("contractSupplier", Model.of(contract.getAwardee())));
            add(new Label("contractTenderId", Model.of(tender.getTenderNumber())));
            addStatus("contractStatus", contract.getStatus().toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, contract.getId());
            createLink("editContract", pageParameters, "btn-edit", EditContractPage.class, awardAcceptance != null);
        } else {
            add(new Label("contractSupplier", Model.of("")));
            add(new Label("contractTenderId", Model.of("")));
            addStatus("contractStatus", DBConstants.Status.NOT_STARTED.toLowerCase());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            if (tenderQuotationEvaluation != null) {
                pageParameters.set(WebConstants.PARAM_TENDER_OPENING_ID, tenderQuotationEvaluation.getId());
            }
            createLink("editContract", pageParameters, "btn-add no-text", EditContractPage.class,
                    awardAcceptance != null);
        }

    }

}
