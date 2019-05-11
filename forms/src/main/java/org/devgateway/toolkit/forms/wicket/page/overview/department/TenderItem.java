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
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
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

    public TenderItem(final String id, final PurchaseRequisition purchaseRequisition) {
        super(id);
        this.purchaseRequisition = purchaseRequisition;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
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

        /*
         * header.add(new Label("projectTitle", new
         * Model<String>(this.project.getProjectTitle()))); header.add(new
         * Label("fiscalYear", new
         * PropertyModel<String>(this.project.getProcurementPlan().getFiscalYear(),
         * "label")));
         */
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
        Tender tender = tenderService.findByPurchaseRequisition(purchaseRequisition);
        if (tender != null) {
            add(new Label("tenderTitle", Model.of(tender.getTenderTitle())));
            add(new Label("tenderId", Model.of(tender.getTenderNumber())));
            add(new Label("amount", Model.of(0)));
            Label statusLabel = new Label("tenderStatus");
            statusLabel.add(AttributeAppender.append("class", tender.getStatus().toLowerCase()));
            add(statusLabel);
            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, tender.getId());
            final BootstrapBookmarkablePageLink<Void> tenderEdit = new BootstrapBookmarkablePageLink<Void>("editTender",
                    EditTenderPage.class, pageParameters, Buttons.Type.Success);
            tenderEdit.add(AttributeAppender.append("class", "btn-edit"));
            add(tenderEdit);
        } else {
            add(new Label("tenderTitle", Model.of("")));
            add(new Label("tenderId", Model.of("")));
            add(new Label("amount", Model.of(0)));
            Label statusLabel = new Label("tenderStatus");
            statusLabel.add(AttributeAppender.append("class", "not_started"));
            add(statusLabel);

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, null);
            pageParameters.set(WebConstants.PARAM_PURCHASE_REQUISITION_ID, purchaseRequisition.getId());
            final BootstrapBookmarkablePageLink<Void> tenderEdit = new BootstrapBookmarkablePageLink<Void>("editTender",
                    EditTenderPage.class, pageParameters, Buttons.Type.Success);
            tenderEdit.add(AttributeAppender.append("class", "btn-add no-text"));
            add(tenderEdit);
        }

    }

    private void addTenderEvaluationSection() {
        TenderQuotationEvaluation tenderQuotationEvaluation = tenderQuotationEvalutionService
                .findByPurchaseRequisition(purchaseRequisition);

    }

    private void addProfessionalOpinionSection() {
        ProfessionalOpinion professionalOpinion = professionalOpinionService
                .findByPurchaseRequisition(purchaseRequisition);

    }

    private void addAwardNotificationSection() {
        AwardNotification awardNotification = awardNotificationService.findByPurchaseRequisition(purchaseRequisition);

    }

    private void addAwardAcceptanceSection() {
        AwardAcceptance awardAcceptance = awardAcceptanceService.findByPurchaseRequisition(purchaseRequisition);

    }

    private void addContractSection() {
        Contract contract = contractService.findByPurchaseRequisition(purchaseRequisition);

    }

}
