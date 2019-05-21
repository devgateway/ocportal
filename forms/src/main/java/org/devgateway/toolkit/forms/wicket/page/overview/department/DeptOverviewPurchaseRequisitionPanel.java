package org.devgateway.toolkit.forms.wicket.page.overview.department;

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

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

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
        add(new DeptOverviewStatusLabel("requisitionStatus", purchaseRequisition));
        createLinkNoPrevStep(purchaseRequisition, "requisitionEdit", EditPurchaseRequisitionPage.class);
    }


    private void addTenderDocumentSection() {
        add(new Label("tenderTitle", tender != null ? tender.getTenderTitle() : ""));
        add(new Label("tenderId", tender != null ? tender.getTenderNumber() : ""));
        add(new Label("amount", tender != null ? tender.getTotalAmount() : ""));
        add(new DeptOverviewStatusLabel("tenderStatus", tender));
        createLinkNoPrevStep(tender, "editTender", EditTenderPage.class);
    }

    boolean canEdit(Statusable previousStep) {
        return previousStep != null && (previousStep.getStatus().equals(DBConstants.Status.SUBMITTED)
                || previousStep.getStatus().equals(DBConstants.Status.APPROVED));
    }

    private BootstrapAjaxLink<Void> createLinkNoPrevStep(GenericPersistable persistable, final String id,
                                                                     final Class<? extends AbstractEditPage> clazz) {
        final PageParameters pageParameters = new PageParameters();
        if (persistable != null) {
            pageParameters.set(WebConstants.PARAM_ID, persistable.getId());
        }      

        final BootstrapAjaxLink<Void> button = new BootstrapAjaxLink<Void>(id,
                Buttons.Type.Success) {       
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                SessionUtil.setSessionPurchaseRequisition(purchaseRequisition);
                setResponsePage(clazz, pageParameters);                
            }            
        };
        
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
        add(new DeptOverviewStatusLabel("tenderOpeningStatus", tenderQuotationEvaluation));
        createLink(tenderQuotationEvaluation, "editTenderOpening", EditTenderQuotationEvaluationPage.class, tender);
    }

    private void addProfessionalOpinionSection() {
        add(new Label("professionalOpinionSupplier", professionalOpinion != null
                ? professionalOpinion.getAwardee() : ""));
        add(new Label("professionalOpinionAmount", professionalOpinion != null
                ? professionalOpinion.getRecommendedAwardAmount() : ""));
        add(new DeptOverviewStatusLabel("professionalOpinionStatus", professionalOpinion));
        createLink(professionalOpinion, "editProfessionalOpinion",
                EditProfessionalOpinionPage.class, tenderQuotationEvaluation);
    }

    private void addAwardNotificationSection() {
        add(new Label("awardNotificationSupplier", awardNotification != null ? awardNotification.getAwardee() : ""));
        add(new Label("awardNotificationTenderId", awardNotification != null ? tender.getTenderNumber() : ""));
        add(new DeptOverviewStatusLabel("awardNotificationStatus", awardNotification));
        createLink(awardNotification, "editAwardNotification", EditAwardNotificationPage.class, professionalOpinion);
    }

    private void addAwardAcceptanceSection() {
        add(new Label("awardAcceptanceSupplier", awardAcceptance != null ? awardAcceptance.getAwardee() : ""));
        add(new Label("awardAcceptanceTenderId", awardAcceptance != null ? tender.getTenderNumber() : ""));
        add(new DeptOverviewStatusLabel("awardAcceptanceStatus", awardAcceptance));
        createLink(awardAcceptance, "editAwardAcceptance", EditAwardAcceptancePage.class, awardNotification);
    }

    private void addContractSection() {
        add(new Label("contractSupplier", contract != null ? contract.getAwardee() : ""));
        add(new Label("contractTenderId", contract != null ? tender.getTenderNumber() : ""));
        add(new DeptOverviewStatusLabel("contractStatus", contract));
        createLink(contract, "editContract", EditContractPage.class, awardAcceptance);
    }
}
