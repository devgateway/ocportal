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
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderQuotationEvaluationPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;

// TODO - this class should be renamed
public class DeptOverviewPurchaseRequisitionPanel extends Panel {

    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    private final PurchaseRequisition purchaseRequisition;

    private Tender tender;

    private TenderQuotationEvaluation tenderQuotationEvaluation;

    private ProfessionalOpinion professionalOpinion;

    private AwardNotification awardNotification;

    private AwardAcceptance awardAcceptance;

    private Contract contract;

    private Boolean expanded = false;

    public DeptOverviewPurchaseRequisitionPanel(final String id, final PurchaseRequisition purchaseRequisition) {
        super(id);
        this.purchaseRequisition = purchaseRequisition;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        PurchaseRequisition pr = purchaseRequisitionService.findById(purchaseRequisition.getId()).get();
        tender = pr.getTender();
        tenderQuotationEvaluation = pr.getTenderQuotationEvaluation();
        professionalOpinion = pr.getProfessionalOpinion();
        awardNotification = pr.getAwardNotification();
        awardAcceptance = pr.getAwardAcceptance();
        contract = pr.getContract();

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


    private void addTenderDocumentSection() {
        add(new Label("tenderTitle", tender != null ? tender.getTenderTitle() : ""));
        add(new Label("tenderId", tender != null ? tender.getTenderNumber() : ""));
        add(new Label("amount", tender != null ? tender.getTotalAmount() : ""));
        addStatus("tenderStatus", tender != null
                ? tender.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase());

    boolean canEdit(Statusable previousStep) {
        return previousStep != null && (previousStep.getStatus().equals(DBConstants.Status.SUBMITTED) ||
                previousStep.getStatus().equals(DBConstants.Status.APPROVED));
    }

    private BootstrapBookmarkablePageLink<Void> createLinkNoPrevStep(GenericPersistable persistable, final String id,
                                                                     final Class<? extends AbstractEditPage> clazz) {
        final PageParameters pageParameters = new PageParameters();
        if (persistable != null) {
            pageParameters.set(WebConstants.PARAM_ID, persistable.getId());
        }

        SessionUtil.setSessionPurchaseRequisition(purchaseRequisition);

        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<Void>(id, clazz,
                pageParameters, Buttons.Type.Success
        );
        button.add(AttributeAppender.append("class", "no-text btn-"
                + (persistable == null ? "add" : "edit")));
        add(button);
        return button;
    }

    private void createLink(GenericPersistable persistable, final String id,
                            final Class<? extends AbstractEditPage> clazz, Statusable previousStep) {
        createLinkNoPrevStep(persistable, id, clazz).setEnabled(canEdit(previousStep));
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
