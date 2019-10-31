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
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderProcessPage;
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
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
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
public class ListViewTenderProcessOverview extends AbstractListViewStatus<TenderProcess> {
    protected static final Logger logger = LoggerFactory.getLogger(DataEntryBasePage.class);

    private final Boolean canAccessAddNewButtonInDeptOverview;

    private final TenderProcess sessionTenderProcess;

    public ListViewTenderProcessOverview(final String id,
                                         final IModel<List<TenderProcess>> model,
                                         final TenderProcess sessionTenderProcess) {
        super(id, model);

        this.sessionTenderProcess = sessionTenderProcess;

        // check if we need to expand a Purchase Requisition
        if (sessionTenderProcess != null) {
            expandedContainerIds.add(sessionTenderProcess.getId());
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
        if (this.getModelObject() != null && sessionTenderProcess != null) {
            if (this.getModelObject().contains(sessionTenderProcess)) {
                response.render(OnDomReadyHeaderItem.forScript(JQueryUtil.animateScrollTop("#" + "purchasereq-header-"
                        + sessionTenderProcess.getId(), 100, 500)));
            }
        }
    }

    @Override
    protected void populateCompoundListItem(final ListItem<TenderProcess> item) {
    }

    @Override
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<TenderProcess> item) {
        header.add(AttributeAppender.append("class", "tender"));   // add specific class to tender overview header
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);
        headerFragment.setMarkupId("purchasereq-header-" + item.getModelObject().getId());

        headerFragment.add(new Label("title", "Tender Process " + (item.getIndex() + 1)));

        WebMarkupContainer terminatedRequisition = new WebMarkupContainer("terminatedRequisition");
        terminatedRequisition.setVisibilityAllowed(item.getModelObject().isTerminated());
        headerFragment.add(terminatedRequisition);

        header.add(headerFragment);
    }

    @Override
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<TenderProcess> item) {
        hideableContainer.add(AttributeAppender.append("class", "purchase-req")); // add specific class to pr list
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);

        final TenderProcess tenderProcess = item.getModelObject();
        final Tender tender = PersistenceUtil.getNext(tenderProcess.getTender());
        final TenderQuotationEvaluation tenderQuotationEvaluation = PersistenceUtil.getNext(tenderProcess
                .getTenderQuotationEvaluation());
        final ProfessionalOpinion professionalOpinion = PersistenceUtil.getNext(
                tenderProcess.getProfessionalOpinion());
        final AwardNotification awardNotification = PersistenceUtil.getNext(tenderProcess.getAwardNotification());
        final AwardAcceptance awardAcceptance = PersistenceUtil.getNext(tenderProcess.getAwardAcceptance());
        final Contract contract = PersistenceUtil.getNext(tenderProcess.getContract());

        final Panel requisitionPanel = new TenderDetailPanel<>("requisitionPanel", tenderProcess,
                tenderProcess.getPurchaseRequestNumber(), new ArrayList<>(Arrays.asList(
                tenderProcess.getRequestApprovalDate(), tenderProcess.getAmount())),
                tenderProcess, EditTenderProcessPage.class, null);
        containerFragment.add(requisitionPanel);

        final Panel tenderPanel = new TenderDetailPanel<>("tenderPanel", tender,
                "Tender Document", tender != null ? new ArrayList<>(Arrays.asList(
                tender.getTenderTitle(), tender.getTenderNumber(), tender.getTenderValue())) : null,
                tenderProcess, EditTenderPage.class, tenderProcess);
        containerFragment.add(tenderPanel);

        final Panel evaluationPanel = new TenderDetailPanel("evaluationPanel", tenderQuotationEvaluation,
                "Quotation and Evaluation", (tenderQuotationEvaluation != null && tender != null)
                ? new ArrayList<>(Arrays.asList(tender.getTenderTitle(), tender.getTenderNumber())) : null,
                tenderProcess, EditTenderQuotationEvaluationPage.class, tender);
        containerFragment.add(evaluationPanel);

        final Panel professionalOpinionPanel = new TenderDetailPanel<>("professionalOpinionPanel", professionalOpinion,
                "Professional Opinion", professionalOpinion != null ? new ArrayList<>(Arrays.asList(
                professionalOpinion.getAwardee(), professionalOpinion.getRecommendedAwardAmount())) : null,
                tenderProcess, EditProfessionalOpinionPage.class, tenderQuotationEvaluation);
        containerFragment.add(professionalOpinionPanel);

        final Panel awardNotificationPanel = new TenderDetailPanel<>("awardNotificationPanel", awardNotification,
                "Notification", awardNotification != null ? new ArrayList<>(Arrays.asList(
                awardNotification.getAwardee(), awardNotification.getAwardValue())) : null,
                tenderProcess, EditAwardNotificationPage.class, professionalOpinion);
        containerFragment.add(awardNotificationPanel);

        final Panel awardAcceptancePanel = new TenderDetailPanel<>("awardAcceptancePanel", awardAcceptance,
                "Acceptance", awardAcceptance != null ? new ArrayList<>(Arrays.asList(
                awardAcceptance.getAwardee(), awardAcceptance.getAcceptedAwardValue())) : null,
                tenderProcess, EditAwardAcceptancePage.class, awardNotification);
        containerFragment.add(awardAcceptancePanel);

        final Panel contractPanel = new TenderDetailPanel<>("contractPanel", contract,
                "Contracts", contract != null ? new ArrayList<>(Arrays.asList(
                contract.getAwardee(), contract.getContractValue())) : null,
                tenderProcess, EditContractPage.class, awardAcceptance);
        containerFragment.add(contractPanel);

        hideableContainer.add(containerFragment);
    }

    @Override
    protected Long getItemId(ListItem<TenderProcess> item) {
        return item.getModelObject().getId();
    }

    private boolean canEdit(final TenderProcess tenderProcess,
                            final AbstractStatusAuditableEntity persistable,
                            final Statusable previousStep) {

        //terminated can always edit
        if (persistable != null && persistable.getStatus().equals(DBConstants.Status.TERMINATED)) {
            return true;
        }

        //the rest of the steps of a terminated chain, can never be edited
        if (persistable == null && tenderProcess.isTerminated()) {
            return false;
        }

        return previousStep != null && (previousStep.getStatus().equals(DBConstants.Status.SUBMITTED)
                || previousStep.getStatus().equals(DBConstants.Status.APPROVED));
    }

    private class TenderDetailPanel<T extends AbstractMakueniEntity> extends GenericPanel<T> {
        private final T entity;

        private final String tenderLabel;

        private final List<Object> tenderInfo;

        private final TenderProcess tenderProcess;

        private final Class<? extends AbstractEditPage> editClazz;
        private final Statusable previousStep;

        TenderDetailPanel(final String id, final T entity, final String tenderLabel,
                          final List<Object> tenderInfo, final TenderProcess tenderProcess,
                          final Class<? extends AbstractEditPage> editClazz, Statusable previousStep) {
            super(id);

            this.entity = entity;
            this.previousStep = previousStep;
            this.tenderLabel = tenderLabel;
            this.tenderInfo = tenderInfo;
            this.tenderProcess = tenderProcess;
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

                    sessionMetadataService.setSessionTenderProcess(tenderProcess);
                    sessionMetadataService.setSessionProject(tenderProcess.getProject());
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
            if (!(entity instanceof TenderProcess) && !(entity instanceof Tender)) {
                editTender.setEnabled(canEdit(tenderProcess, entity, previousStep));
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