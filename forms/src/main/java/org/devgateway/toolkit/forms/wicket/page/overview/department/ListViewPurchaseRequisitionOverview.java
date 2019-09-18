package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.util.JQueryUtil;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderQuotationEvaluationPage;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewPurchaseRequisitionOverview extends AbstractListViewStatus<PurchaseRequisition> {
    protected static final Logger logger = LoggerFactory.getLogger(DataEntryBasePage.class);

    private final Boolean canAccessAddNewButtonInDeptOverview;

    private final PurchaseRequisition sessionPurchaseRequisition;

    public ListViewPurchaseRequisitionOverview(final String id,
                                               final IModel<List<PurchaseRequisition>> model,
                                               final PurchaseRequisition sessionPurchaseRequisition) {
        super(id, model);

        this.sessionPurchaseRequisition = sessionPurchaseRequisition;

        // check if we need to expand a Purchase Requisition
        if (sessionPurchaseRequisition != null) {
            expandedContainerIds.add(sessionPurchaseRequisition.getId());
        }

        canAccessAddNewButtonInDeptOverview = ComponentUtil.canAccessAddNewButtonInDeptOverview(sessionMetadataService);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        listWrapper.add(AttributeAppender.replace("class", "tender-list-wrapper"));
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // scroll to the last edited item (see: OCMAKU-135)
        if (this.getModelObject() != null && sessionPurchaseRequisition != null) {
            if (this.getModelObject().contains(sessionPurchaseRequisition)) {
                response.render(OnDomReadyHeaderItem.forScript(JQueryUtil.animateScrollTop("#" + "purchasereq-header-"
                        + sessionPurchaseRequisition.getId(), 100, 500)));
            }
        }
    }

    @Override
    protected void populateCompoundListItem(final ListItem<PurchaseRequisition> item) {
    }

    @Override
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<PurchaseRequisition> item) {
        header.add(AttributeAppender.append("class", "tender"));   // add specific class to tender overview header
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);
        headerFragment.setMarkupId("purchasereq-header-" + item.getModelObject().getId());

        headerFragment.add(new Label("title", "Purchase Requisition " + (item.getIndex() + 1)));

        header.add(headerFragment);
    }

    @Override
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<PurchaseRequisition> item) {
        hideableContainer.add(AttributeAppender.append("class", "purchase-req")); // add specific class to pr list
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);

        final PurchaseRequisition purchaseRequisition = item.getModelObject();
        final Tender tender = PersistenceUtil.getNext(purchaseRequisition.getTender());
        final TenderQuotationEvaluation tenderQuotationEvaluation = PersistenceUtil.getNext(purchaseRequisition
                .getTenderQuotationEvaluation());
        final ProfessionalOpinion professionalOpinion = PersistenceUtil.getNext(
                purchaseRequisition.getProfessionalOpinion());
        final AwardNotification awardNotification = PersistenceUtil.getNext(purchaseRequisition.getAwardNotification());
        final AwardAcceptance awardAcceptance = PersistenceUtil.getNext(purchaseRequisition.getAwardAcceptance());
        final Contract contract = PersistenceUtil.getNext(purchaseRequisition.getContract());

        final Panel requisitionPanel = new TenderDetailPanel<>("requisitionPanel", purchaseRequisition,
                purchaseRequisition.getPurchaseRequestNumber(), new ArrayList<>(Arrays.asList(
                purchaseRequisition.getRequestApprovalDate(), purchaseRequisition.getAmount())),
                purchaseRequisition, EditPurchaseRequisitionPage.class, null);
        containerFragment.add(requisitionPanel);

        final Panel tenderPanel = new TenderDetailPanel<>("tenderPanel", tender,
                "Tender Document", tender != null ? new ArrayList<>(Arrays.asList(
                tender.getTenderTitle(), tender.getTenderNumber(), tender.getTenderValue())) : null,
                purchaseRequisition, EditTenderPage.class, purchaseRequisition);
        containerFragment.add(tenderPanel);

        final Panel evaluationPanel = new TenderDetailPanel("evaluationPanel", tenderQuotationEvaluation,
                "Quotation and Evaluation", (tenderQuotationEvaluation != null && tender != null)
                ? new ArrayList<>(Arrays.asList(tender.getTenderTitle(), tender.getTenderNumber())) : null,
                purchaseRequisition, EditTenderQuotationEvaluationPage.class, tender);
        containerFragment.add(evaluationPanel);

        final Panel professionalOpinionPanel = new TenderDetailPanel<>("professionalOpinionPanel", professionalOpinion,
                "Professional Opinion", professionalOpinion != null ? new ArrayList<>(Arrays.asList(
                professionalOpinion.getAwardee(), professionalOpinion.getRecommendedAwardAmount())) : null,
                purchaseRequisition, EditProfessionalOpinionPage.class, tenderQuotationEvaluation);
        containerFragment.add(professionalOpinionPanel);

        final Panel awardNotificationPanel = new TenderDetailPanel<>("awardNotificationPanel", awardNotification,
                "Notification", awardNotification != null ? new ArrayList<>(Arrays.asList(
                awardNotification.getAwardee(), awardNotification.getAwardValue())) : null,
                purchaseRequisition, EditAwardNotificationPage.class, professionalOpinion);
        containerFragment.add(awardNotificationPanel);

        final Panel awardAcceptancePanel = new TenderDetailPanel<>("awardAcceptancePanel", awardAcceptance,
                "Acceptance", awardAcceptance != null ? new ArrayList<>(Arrays.asList(
                awardAcceptance.getAwardee(), awardAcceptance.getAcceptedAwardValue())) : null,
                purchaseRequisition, EditAwardAcceptancePage.class, awardNotification);
        containerFragment.add(awardAcceptancePanel);

        final Panel contractPanel = new TenderDetailPanel<>("contractPanel", contract,
                "Contracts", contract != null ? new ArrayList<>(Arrays.asList(
                contract.getAwardee(), contract.getContractValue())) : null,
                purchaseRequisition, EditContractPage.class, awardAcceptance);
        containerFragment.add(contractPanel);

        hideableContainer.add(containerFragment);
    }

    @Override
    protected Long getItemId(ListItem<PurchaseRequisition> item) {
        return item.getModelObject().getId();
    }

    private boolean canEdit(final PurchaseRequisition purchaseRequisition,
                            final AbstractStatusAuditableEntity persistable,
                            final Statusable previousStep) {

        //terminated can always edit
        if (persistable != null && persistable.getStatus().equals(DBConstants.Status.TERMINATED)) {
            return true;
        }

        //the rest of the steps of a terminated chain, can never be edited
        if (persistable == null && purchaseRequisition.isTerminated()) {
            return false;
        }

        return previousStep != null && (previousStep.getStatus().equals(DBConstants.Status.SUBMITTED)
                || previousStep.getStatus().equals(DBConstants.Status.APPROVED));
    }

    private class TenderDetailPanel<T extends AbstractMakueniEntity> extends GenericPanel<T> {
        private final T entity;

        private final String tenderLabel;

        private final List<Object> tenderInfo;

        private final PurchaseRequisition purchaseRequisition;

        private final Class<? extends AbstractEditPage> editClazz;
        private final Statusable previousStep;

        TenderDetailPanel(final String id, final T entity, final String tenderLabel,
                          final List<Object> tenderInfo, final PurchaseRequisition purchaseRequisition,
                          final Class<? extends AbstractEditPage> editClazz, Statusable previousStep) {
            super(id);

            this.entity = entity;
            this.previousStep = previousStep;
            this.tenderLabel = tenderLabel;
            this.tenderInfo = tenderInfo;
            this.purchaseRequisition = purchaseRequisition;
            this.editClazz = editClazz;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            add(new Label("tenderLabel", tenderLabel));
            add(new DeptOverviewStatusLabel("tenderStatus", entity));

            final BootstrapAjaxLink<Void> editTender = new BootstrapAjaxLink<Void>("editTender",
                    Buttons.Type.Success) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    final PageParameters pageParameters = new PageParameters();
                    if (entity != null) {
                        pageParameters.set(WebConstants.PARAM_ID, entity.getId());
                    }

                    sessionMetadataService.setSessionPurchaseRequisition(purchaseRequisition);
                    sessionMetadataService.setSessionProject(purchaseRequisition.getProject());
                    setResponsePage(editClazz, pageParameters);
                }
            };

            final String buttonType;
            if (canAccessAddNewButtonInDeptOverview) {
                if (entity == null) {
                    buttonType = "add";
                } else {
                    buttonType = "edit";
                }
            } else {
                buttonType = "view";
            }
            editTender.add(AttributeAppender.append("class", "no-text btn-" + buttonType));

            editTender.add(new TooltipBehavior(Model.of((entity == null
                    ? "Add " : (canAccessAddNewButtonInDeptOverview ? "Edit " : "View "))
                    + StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                    editClazz.getSimpleName().replaceAll("Edit", "").replaceAll("Page", "")), ' '))));

            if (entity == null) {
                editTender.setVisibilityAllowed(canAccessAddNewButtonInDeptOverview);
            }
            if (!(entity instanceof PurchaseRequisition) && !(entity instanceof Tender)) {
                editTender.setEnabled(canEdit(purchaseRequisition, entity, previousStep));
            }
            add(editTender);


            add(new ListView<Object>("tenderInfo", new ListModel<>(tenderInfo)) {
                @Override
                protected void populateItem(final ListItem<Object> item) {
                    final Object object = item.getModelObject();
                    item.add(new Label("item", object != null ? object.toString() : ""));
                }
            });
        }
    }
}