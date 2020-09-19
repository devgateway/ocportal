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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.util.JQueryUtil;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAdministratorReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditInspectionReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditMEReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPMCReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPaymentVoucherPage;
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
import org.devgateway.toolkit.persistence.dao.form.PurchRequisition;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewTenderProcessOverview extends AbstractListViewStatus<TenderProcess> {
    protected static final Logger logger = LoggerFactory.getLogger(DataEntryBasePage.class);

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    @SpringBean
    private TenderProcessService tenderProcessService;

    private final TenderProcess sessionTenderProcess;


    private final SimpleDateFormat formatter = new SimpleDateFormat(DBConstants.DATE_FORMAT);

    public ListViewTenderProcessOverview(final String id,
                                         final IModel<List<TenderProcess>> model,
                                         final TenderProcess sessionTenderProcess) {
        super(id, model);

        this.sessionTenderProcess = sessionTenderProcess;

        // check if we need to expand a Purchase Requisition
        if (sessionTenderProcess != null) {
            expandedContainerIds.add(sessionTenderProcess.getId());
        }
    }

    public boolean canAccessAddNewButtons(Class<? extends AbstractEditPage<?>> editClazz) {
        return ComponentUtil.canAccessAddNewButtons(editClazz, permissionEntityRenderableService,
                sessionMetadataService);
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
    @Transactional(readOnly = true)
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<TenderProcess> item) {
        header.add(AttributeAppender.append("class", "tender"));   // add specific class to tender overview header
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);
        headerFragment.setMarkupId("purchasereq-header-" + item.getModelObject().getId());

        headerFragment.add(new Label("title", "Tender Process " + (item.getIndex() + 1)));

        WebMarkupContainer terminatedRequisition = new WebMarkupContainer("terminatedRequisition");

        TenderProcess tenderProcess = tenderProcessService.findById(item.getModelObject().getId()).get();
        terminatedRequisition.setVisibilityAllowed(tenderProcess.isTerminated());
        headerFragment.add(terminatedRequisition);

        header.add(headerFragment);
    }

    @Override
    @Transactional(readOnly = true)
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<TenderProcess> item, boolean expanded) {

        hideableContainer.add(AttributeAppender.append("class", "purchase-req")); // add specific class to pr list


        if (expanded) {
            final Fragment containerFragment = new Fragment(containerFragmentId, "nonEmptyContainerFragment", this);
            TenderProcess tenderProcess = tenderProcessService.findById(item.getModelObject().getId()).get();

            final Tender tender = PersistenceUtil.getNext(tenderProcess.getTender());
            final TenderQuotationEvaluation tenderQuotationEvaluation = PersistenceUtil.getNext(tenderProcess
                    .getTenderQuotationEvaluation());
            final ProfessionalOpinion professionalOpinion = PersistenceUtil.getNext(
                    tenderProcess.getProfessionalOpinion());
            final AwardNotification awardNotification = PersistenceUtil.getNext(tenderProcess.getAwardNotification());
            final AwardAcceptance awardAcceptance = PersistenceUtil.getNext(tenderProcess.getAwardAcceptance());
            final Contract contract = PersistenceUtil.getNext(tenderProcess.getContract());

            final Panel requisitionPanel = new TenderDetailPanel<>("requisitionPanel",
                    Collections.singletonList(tenderProcess),
                    "Purchase Requisition", tp -> Arrays.asList(
                    tenderProcess.getPurchaseRequestNumber(),
                    tp.getPurchRequisitions().stream().map(PurchRequisition::getRequestApprovalDate)
                            .filter(Objects::nonNull).map(formatter::format).collect(Collectors.toList()),
                    tp.getAmount()
            ), tenderProcess, EditTenderProcessPage.class, null, false
            );
            containerFragment.add(requisitionPanel);

            final Panel tenderPanel = new TenderDetailPanel<>("tenderPanel", Collections.singletonList(tender),
                    "Tender Document", t ->
                    t != null ? (Arrays.asList(t.getTenderTitle(), t.getTenderNumber(), t.getTenderValue())) : null,
                    tenderProcess, EditTenderPage.class, tenderProcess, false);
            containerFragment.add(tenderPanel);

            final Panel evaluationPanel = new TenderDetailPanel<>("evaluationPanel",
                    Collections.singletonList(tenderQuotationEvaluation),
                    "Quotation and Evaluation",
                    qe -> (qe != null && tender != null)
                            ? new ArrayList<>(Arrays.asList(tender.getTenderTitle(), tender.getTenderNumber())) : null,
                    tenderProcess, EditTenderQuotationEvaluationPage.class, tender, false);
            containerFragment.add(evaluationPanel);

            final Panel professionalOpinionPanel = new TenderDetailPanel<>("professionalOpinionPanel",
                    Collections.singletonList(professionalOpinion), "Professional Opinion",
                    po -> po != null ? po.getItems()
                            .stream()
                            .map(Objects::toString)
                            .collect(Collectors.toList())
                            : null,
                    tenderProcess, EditProfessionalOpinionPage.class, tenderQuotationEvaluation, false);
            containerFragment.add(professionalOpinionPanel);

            final Panel awardNotificationPanel = new TenderDetailPanel<>("awardNotificationPanel",
                    Collections.singletonList(awardNotification),
                    "Notification",
                    an -> an != null ? an.getItems()
                            .stream()
                            .map(Objects::toString)
                            .collect(Collectors.toList()) : null,
                    tenderProcess, EditAwardNotificationPage.class, professionalOpinion, false);
            containerFragment.add(awardNotificationPanel);

            final Panel awardAcceptancePanel = new TenderDetailPanel<>("awardAcceptancePanel",
                    Collections.singletonList(awardAcceptance), "Acceptance",
                    aa -> aa != null ? aa.getItems().stream().map(Objects::toString)
                            .collect(Collectors.toList()) : null,
                    tenderProcess, EditAwardAcceptancePage.class, awardNotification, false
            );
            containerFragment.add(awardAcceptancePanel);

            final Panel contractPanel = new TenderDetailPanel<>("contractPanel", Collections.singletonList(contract),
                    "Contracts", c -> c != null ? new ArrayList<>(Arrays.asList(c.getAwardee(),
                    c.getContractValue())) : null, tenderProcess, EditContractPage.class, awardAcceptance, false
            );
            containerFragment.add(contractPanel);

            final Panel administratorReportPanel = new TenderDetailPanel<>("administratorReportPanel",
                    tenderProcess.getAdministratorReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    "Administrator Report", ar -> Collections.singletonList(ar.getLabel()),
                    tenderProcess, EditAdministratorReportPage.class, contract, true
            );
            containerFragment.add(administratorReportPanel);

            final Panel inspectionReportPanel = new TenderDetailPanel<>("inspectionReportPanel",
                    tenderProcess.getInspectionReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    "Inspection Report", ir -> Collections.singletonList(ir.getLabel()),
                    tenderProcess, EditInspectionReportPage.class, contract, true
            );
            containerFragment.add(inspectionReportPanel);

            final Panel pmcReportPanel = new TenderDetailPanel<>("pmcReportPanel",
                    tenderProcess.getPmcReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    "PMC Report", pmc -> Collections.singletonList(pmc.getLabel()),
                    tenderProcess, EditPMCReportPage.class, contract, true
            );
            containerFragment.add(pmcReportPanel);

            final Panel meReportPanel = new TenderDetailPanel<>("meReportPanel",
                    tenderProcess.getMeReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    "M&E Report", me -> Collections.singletonList(me.getLabel()),
                    tenderProcess, EditMEReportPage.class, contract, true
            );
            containerFragment.add(meReportPanel);

            final Panel paymentVoucherPanel = new TenderDetailPanel<>("paymentVoucherPanel",
                    tenderProcess.getPaymentVouchers().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    "Payment Voucher", pv -> Collections.singletonList(pv.getLabel()),
                    tenderProcess, EditPaymentVoucherPage.class, contract, true
            );
            containerFragment.add(paymentVoucherPanel);
            hideableContainer.addOrReplace(containerFragment);
        } else {
            final Fragment containerFragment = new Fragment(containerFragmentId, "emptyContainerFragment", this);
            hideableContainer.addOrReplace(containerFragment);
        }


    }

    @Override
    protected Long getItemId(ListItem<TenderProcess> item) {
        return item.getModelObject().getId();
    }

    @Transactional(readOnly = true)
    private boolean canEdit(final TenderProcess tenderProcess2,
                            final AbstractStatusAuditableEntity persistable,
                            final Statusable previousStep) {

        TenderProcess tenderProcess = tenderProcessService.findById(tenderProcess2.getId()).get();

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
        private final List<T> entities;

        private final String tenderLabel;

        private final SerializableFunction<T, List<Object>> tenderInfo;

        private final TenderProcess tenderProcess;

        private final Class<? extends AbstractEditPage<?>> editClazz;
        private final Statusable previousStep;

        private boolean multiple;

        TenderDetailPanel(final String id, final List<T> entities, final String tenderLabel,
                          final SerializableFunction<T, List<Object>> tenderInfo, final TenderProcess tenderProcess,
                          final Class<? extends AbstractEditPage<?>> editClazz, Statusable previousStep,
                          boolean multiple) {
            super(id);

            this.multiple = multiple;
            this.entities = entities;
            this.previousStep = previousStep;
            this.tenderLabel = tenderLabel;
            this.tenderInfo = tenderInfo;
            this.tenderProcess = tenderProcess;
            this.editClazz = editClazz;
        }

        protected BootstrapAjaxLink<Void> createEditButton(T itemObj) {
            return new BootstrapAjaxLink<Void>("editButton",
                    Buttons.Type.Success) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    final PageParameters pageParameters = new PageParameters();
                    if (itemObj != null) {
                        pageParameters.set(WebConstants.PARAM_ID, itemObj.getId());
                    }

                    sessionMetadataService.setSessionTenderProcess(tenderProcess);
                    sessionMetadataService.setSessionProject(tenderProcess.getProject());
                    setResponsePage(editClazz, pageParameters);
                }
            };
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            add(new Label("tenderLabel", tenderLabel));

            add(new ListView<T>("entities", new ListModel<>(entities)) {
                @Override
                protected void populateItem(final ListItem<T> item) {

                    final T itemObj = item.getModelObject();
                    item.add(new DeptOverviewStatusLabel("tenderStatus", itemObj));

                    BootstrapAjaxLink<Void> editTender = createEditButton(itemObj);

                    final String buttonType;
                    if (canAccessAddNewButtons(editClazz)) {
                        buttonType = "edit";
                    } else {
                        buttonType = "view";
                    }
                    editTender.add(AttributeAppender.append("class", "no-text btn-" + buttonType));

                    editTender.add(new TooltipBehavior(Model.of(canAccessAddNewButtons(editClazz) ? "Edit " : "View "
                            + StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                            editClazz.getSimpleName().replaceAll("Edit", "").replaceAll("Page", "")),
                            ' '))));

                    if (item.getModelObject() == null) {
                        editTender.setVisibilityAllowed(canAccessAddNewButtons(editClazz));
                    }
                    if (!(itemObj instanceof TenderProcess) && !(itemObj instanceof Tender)) {
                        editTender.setEnabled(canEdit(tenderProcess, item.getModelObject(), previousStep));
                    }
                    item.add(editTender);

                    item.add(new ListView<Object>("tenderInfo", new ListModel<>(tenderInfo.apply(itemObj))) {
                        @Override
                        protected void populateItem(final ListItem<Object> item) {
                            final Object object = item.getModelObject();
                            item.add(new Label("item", object != null ? object.toString() : ""));
                        }
                    });
                }
            });

            BootstrapAjaxLink<Void> addButton = createEditButton(null);
            addButton.setVisibilityAllowed(multiple);
            addButton.setEnabled(canAccessAddNewButtons(editClazz) && canEdit(tenderProcess,
                    null, previousStep));
            addButton.add(AttributeAppender.append("class", "no-text btn-add"));
            add(addButton);
        }
    }
}