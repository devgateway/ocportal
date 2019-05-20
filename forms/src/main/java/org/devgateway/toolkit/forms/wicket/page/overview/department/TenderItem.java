package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
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
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.persistence.service.form.TenderService;

// TODO - this class should be renamed
public class TenderItem extends Panel {
    @SpringBean
    private TenderService tenderService;

    @SpringBean
    private TenderQuotationEvaluationService tenderQuotationEvaluationService;

    @SpringBean
    private ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    private AwardNotificationService awardNotificationService;

    @SpringBean
    private AwardAcceptanceService awardAcceptanceService;

    @SpringBean
    private ContractService contractService;

    private final PurchaseRequisition purchaseRequisition;

    private final Project project;

    private Tender tender;

    private TenderQuotationEvaluation tenderQuotationEvaluation;

    private ProfessionalOpinion professionalOpinion;

    private AwardNotification awardNotification;

    private AwardAcceptance awardAcceptance;

    private Contract contract;

    private Boolean expanded = false;

    public TenderItem(final String id, final Project project, final PurchaseRequisition purchaseRequisition) {
        super(id);
        this.project = project;
        this.purchaseRequisition = purchaseRequisition;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        tender = tenderService.findByPurchaseRequisition(purchaseRequisition);
        tenderQuotationEvaluation = tenderQuotationEvaluationService.findByPurchaseRequisition(purchaseRequisition);
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

        header.add(new Label("headerTitle", purchaseRequisition.getTitle()));
        add(header);
    }

    private void addPurchaseRequisitionSection() {
        add(new Label("requisitionTitle", purchaseRequisition.getTitle()));
        add(new Label("requisitionDate", purchaseRequisition.getRequestApprovalDate()));
        add(new Label("requisitionAmount", purchaseRequisition.getAmount()));
        Label statusLabel = new Label("requisitionStatus");
        statusLabel.add(AttributeAppender.append("class", purchaseRequisition.getStatus().toLowerCase()));
        add(statusLabel);

        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, purchaseRequisition.getId());

        final BootstrapBookmarkablePageLink<Void> purchaseRequisitionEdit = new BootstrapBookmarkablePageLink<>(
                "requisitionEdit", EditPurchaseRequisitionPage.class, pageParameters, Buttons.Type.Success);
        add(purchaseRequisitionEdit);

    }

    private void addTenderDocumentSection() {
        add(new Label("tenderTitle", tender != null ? tender.getTenderTitle() : ""));
        add(new Label("tenderId", tender != null ? tender.getTenderNumber() : ""));
        add(new Label("amount", tender != null ? tender.getTotalAmount() : ""));
        addStatus("tenderStatus", tender != null
                ? tender.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

        final PageParameters pageParameters = new PageParameters();
        if (tender != null) {
            pageParameters.set(WebConstants.PARAM_ID, tender.getId());
        }

        createLink("editTender", pageParameters, "btn-add no-text", EditTenderPage.class, true);
    }

    private void createLink(final String id, final PageParameters pageParameters, final String cssClasses,
                            final Class clazz, final boolean enabled) {

        SessionUtil.setSessionPurchaseRequisition(purchaseRequisition);

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
        add(new Label("tenderOpeningTitle", tenderQuotationEvaluation != null ? tender.getTenderTitle() : ""));
        add(new Label("tenderOpeningID", tenderQuotationEvaluation != null ? tender.getTenderNumber() : ""));
        add(new Label("tenderOpeningAmount", tenderQuotationEvaluation != null ? 0 : ""));
        addStatus("tenderOpeningStatus", tenderQuotationEvaluation != null
                ? tenderQuotationEvaluation.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

        final PageParameters pageParameters = new PageParameters();
        if (tenderQuotationEvaluation != null) {
            pageParameters.set(WebConstants.PARAM_ID, tenderQuotationEvaluation.getId());
        }

        createLink("editTenderOpening", pageParameters, "btn-add no-text", EditTenderQuotationEvaluationPage.class,
                tender != null);

    }

    private void addProfessionalOpinionSection() {
        add(new Label("professionalOpinionSupplier", professionalOpinion != null
                ? professionalOpinion.getAwardee() : ""));
        add(new Label("professionalOpinionAmount", professionalOpinion != null
                ? professionalOpinion.getRecommendedAwardAmount() : ""));
        addStatus("professionalOpinionStatus", professionalOpinion != null
                ? professionalOpinion.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

        final PageParameters pageParameters = new PageParameters();
        if (professionalOpinion != null) {
            pageParameters.set(WebConstants.PARAM_ID, professionalOpinion.getId());
        }

        createLink("editProfessionalOpinion", pageParameters, "btn-add no-text", EditProfessionalOpinionPage.class,
                tenderQuotationEvaluation != null);
    }

    private void addAwardNotificationSection() {
        add(new Label("awardNotificationSupplier", awardNotification != null ? awardNotification.getAwardee() : ""));
        add(new Label("awardNotificationTenderId", awardNotification != null ? tender.getTenderNumber() : ""));
        addStatus("awardNotificationStatus", awardNotification != null
                ? awardNotification.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

        final PageParameters pageParameters = new PageParameters();
        if (awardNotification != null) {
            pageParameters.set(WebConstants.PARAM_ID, awardNotification.getId());
        }

        createLink("editAwardNotification", pageParameters, "btn-add no-text", EditAwardNotificationPage.class,
                professionalOpinion != null);
    }

    private void addAwardAcceptanceSection() {
        add(new Label("awardAcceptanceSupplier", awardAcceptance != null ? awardAcceptance.getAwardee() : ""));
        add(new Label("awardAcceptanceTenderId", awardAcceptance != null ? tender.getTenderNumber() : ""));
        addStatus("awardAcceptanceStatus", awardAcceptance != null
                ? awardAcceptance.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

        final PageParameters pageParameters = new PageParameters();
        if (awardAcceptance != null) {
            pageParameters.set(WebConstants.PARAM_ID, awardAcceptance.getId());
        }

        createLink("editAwardAcceptance", pageParameters, "btn-add no-text", EditAwardAcceptancePage.class,
                awardNotification != null);
    }

    private void addContractSection() {
        add(new Label("contractSupplier", contract != null ? contract.getAwardee() : ""));
        add(new Label("contractTenderId", contract != null ? tender.getTenderNumber() : ""));
        addStatus("contractStatus", contract != null
                ? contract.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

        final PageParameters pageParameters = new PageParameters();
        if (contract != null) {
            pageParameters.set(WebConstants.PARAM_ID, contract.getId());
        }

        createLink("editContract", pageParameters, "btn-add no-text", EditContractPage.class,
                awardAcceptance != null);
    }
}
