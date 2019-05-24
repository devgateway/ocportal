package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
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
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
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

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewPurchaseRequisitionOverview extends AbstractListViewStatus<PurchaseRequisition> {
    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    public ListViewPurchaseRequisitionOverview(final String id, final IModel<List<PurchaseRequisition>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        listWrapper.add(AttributeAppender.replace("class", "tender-list-wrapper"));
    }

    @Override
    protected void populateCompoundListItem(final ListItem<PurchaseRequisition> item) {

    }

    @Override
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<PurchaseRequisition> item) {
        header.add(AttributeAppender.append("class", "tender"));   // add specific class to project overview header
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);

        headerFragment.add(new Label("title"));

        header.add(headerFragment);
    }

    @Override
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<PurchaseRequisition> item) {
        hideableContainer.add(AttributeAppender.append("class", "tender")); // add specific class to project list
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);

        final PurchaseRequisition purchaseRequisition = item.getModelObject();
        final Tender tender = purchaseRequisition.getTender();
        final TenderQuotationEvaluation tenderQuotationEvaluation = purchaseRequisition.getTenderQuotationEvaluation();
        final ProfessionalOpinion professionalOpinion = purchaseRequisition.getProfessionalOpinion();
        final AwardNotification awardNotification = purchaseRequisition.getAwardNotification();
        final AwardAcceptance awardAcceptance = purchaseRequisition.getAwardAcceptance();
        final Contract contract = purchaseRequisition.getContract();

        // TODO - refactor this section
        containerFragment.add(new Label("requisitionTitle", purchaseRequisition.getTitle()));
        containerFragment.add(new Label("requisitionDate", purchaseRequisition.getRequestApprovalDate()));
        containerFragment.add(new Label("requisitionAmount", purchaseRequisition.getAmount()));
        containerFragment.add(new DeptOverviewStatusLabel("requisitionStatus", purchaseRequisition));
        createLinkNoPrevStep(containerFragment, purchaseRequisition, purchaseRequisition, "requisitionEdit",
                EditPurchaseRequisitionPage.class);


        containerFragment.add(new Label("tenderTitle", tender != null ? tender.getTenderTitle() : ""));
        containerFragment.add(new Label("tenderId", tender != null ? tender.getTenderNumber() : ""));
        containerFragment.add(new Label("amount", tender != null ? tender.getTenderValue() : ""));
        containerFragment.add(new DeptOverviewStatusLabel("tenderStatus", tender));
        createLinkNoPrevStep(containerFragment, purchaseRequisition, tender, "editTender", EditTenderPage.class);


        containerFragment.add(new Label("tenderOpeningTitle",
                tenderQuotationEvaluation != null ? tender.getTenderTitle() : ""));
        containerFragment.add(new Label("tenderOpeningID",
                tenderQuotationEvaluation != null ? tender.getTenderNumber() : ""));
        containerFragment.add(new Label("tenderOpeningAmount", tenderQuotationEvaluation != null ? 0 : ""));
        containerFragment.add(new DeptOverviewStatusLabel("tenderOpeningStatus", tenderQuotationEvaluation));
        createLink(containerFragment, purchaseRequisition, tenderQuotationEvaluation, "editTenderOpening",
                EditTenderQuotationEvaluationPage.class, tender);


        containerFragment.add(new Label("professionalOpinionSupplier", professionalOpinion != null
                ? professionalOpinion.getAwardee() : ""));
        containerFragment.add(new Label("professionalOpinionAmount", professionalOpinion != null
                ? professionalOpinion.getRecommendedAwardAmount() : ""));
        containerFragment.add(new DeptOverviewStatusLabel("professionalOpinionStatus", professionalOpinion));
        createLink(containerFragment, purchaseRequisition, professionalOpinion, "editProfessionalOpinion",
                EditProfessionalOpinionPage.class, tenderQuotationEvaluation);


        containerFragment.add(new Label("awardNotificationSupplier",
                awardNotification != null ? awardNotification.getAwardee() : ""));
        containerFragment.add(new Label("awardNotificationTenderId",
                awardNotification != null ? tender.getTenderNumber() : ""));
        containerFragment.add(new DeptOverviewStatusLabel("awardNotificationStatus", awardNotification));
        createLink(containerFragment, purchaseRequisition, awardNotification, "editAwardNotification",
                EditAwardNotificationPage.class, professionalOpinion);


        containerFragment.add(new Label("awardAcceptanceSupplier",
                awardAcceptance != null ? awardAcceptance.getAwardee() : ""));
        containerFragment.add(new Label("awardAcceptanceTenderId",
                awardAcceptance != null ? tender.getTenderNumber() : ""));
        containerFragment.add(new DeptOverviewStatusLabel("awardAcceptanceStatus", awardAcceptance));
        createLink(containerFragment, purchaseRequisition, awardAcceptance, "editAwardAcceptance",
                EditAwardAcceptancePage.class, awardNotification);


        containerFragment.add(new Label("contractSupplier", contract != null ? contract.getAwardee() : ""));
        containerFragment.add(new Label("contractTenderId", contract != null ? tender.getTenderNumber() : ""));
        containerFragment.add(new DeptOverviewStatusLabel("contractStatus", contract));
        createLink(containerFragment, purchaseRequisition, contract, "editContract",
                EditContractPage.class, awardAcceptance);


        hideableContainer.add(containerFragment);
    }

    @Override
    protected Long getItemId(ListItem<PurchaseRequisition> item) {
        return item.getModelObject().getId();
    }


    boolean canEdit(Statusable previousStep) {
        return previousStep != null && (previousStep.getStatus().equals(DBConstants.Status.SUBMITTED)
                || previousStep.getStatus().equals(DBConstants.Status.APPROVED));
    }

    private BootstrapAjaxLink<Void> createLinkNoPrevStep(final Fragment containerFragment,
                                                         final PurchaseRequisition purchaseRequisition,
                                                         final GenericPersistable persistable, final String id,
                                                         final Class<? extends AbstractEditPage> clazz) {
        final PageParameters pageParameters = new PageParameters();
        if (persistable != null) {
            pageParameters.set(WebConstants.PARAM_ID, persistable.getId());
        }

        final BootstrapAjaxLink<Void> button = new BootstrapAjaxLink<Void>(id, Buttons.Type.Success) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                SessionUtil.setSessionPurchaseRequisition(purchaseRequisition);
                setResponsePage(clazz, pageParameters);
            }
        };

        button.add(AttributeAppender.append("class", "no-text btn-" + (persistable == null ? "add" : "edit")));
        containerFragment.add(button);
        return button;
    }

    private void createLink(final Fragment containerFragment,
                            final PurchaseRequisition purchaseRequisition,
                            final GenericPersistable persistable, final String id,
                            final Class<? extends AbstractEditPage> clazz, Statusable previousStep) {
        createLinkNoPrevStep(containerFragment, purchaseRequisition, persistable, id, clazz).
                setEnabled(canEdit(previousStep));
    }
}
