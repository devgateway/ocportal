package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
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
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.fm.DgFmBehavior;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.util.JQueryUtil;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.util.EditViewResourceModel;
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
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionGroupPage;
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
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisitionGroup;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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

    public static final int MAX_VISIBLE_TENDER_TITLE_LENGTH = 100;

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    @SpringBean
    private TenderProcessService tenderProcessService;

    @SpringBean
    protected DgFmService fmService;

    private final TenderProcess sessionTenderProcess;

    private final Boolean allowNullProjects;

    private final SimpleDateFormat formatter = new SimpleDateFormat(DBConstants.DATE_FORMAT);

    public ListViewTenderProcessOverview(final String id, boolean allowNullProjects,
                                         final IModel<List<TenderProcess>> model,
                                         final TenderProcess sessionTenderProcess) {
        super(id, model);

        this.allowNullProjects = allowNullProjects;
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

    @Transactional(readOnly = true)
    protected Icon createValidationLabel(ListItem<TenderProcess> item) {
        BindingResult validate = tenderProcessService.validate(item.getModelObject());

        Icon validationWarning = new Icon("validationWarning", FontAwesomeIconType.exclamation_triangle);
        if (validate.getAllErrors().size() > 0) {
            TooltipConfig tooltipConfig = new TooltipConfig();
            tooltipConfig.withPlacement(TooltipConfig.Placement.bottom);
            tooltipConfig.withHtml(true);
            StringBuilder sb = new StringBuilder("<p/>");
            validate.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).forEach(
                    c -> sb.append(" - ").append(c).append("<p/>"));

            TooltipBehavior tooltipBehavior = new TooltipBehavior(Model.of(sb.toString()), tooltipConfig);
            validationWarning.add(tooltipBehavior);
        }

        validationWarning.setVisibilityAllowed(validate.getAllErrors().size() > 0);
        return validationWarning;
    }

    @Override
    @Transactional(readOnly = true)
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<TenderProcess> item) {

        header.add(AttributeAppender.append("class", "tender"));   // add specific class to tender overview header
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);
        headerFragment.setMarkupId("purchasereq-header-" + item.getModelObject().getId());

        Label titleLabel = getTenderProcessTitleLabel((item.getIndex() + 1), item.getModelObject());
        if (item.getModelObject().getSingleTender() != null
                && !StringUtils.isEmpty(item.getModelObject().getSingleTender().getTitle())
                && item.getModelObject().getSingleTender().getTitle().length() > MAX_VISIBLE_TENDER_TITLE_LENGTH) {
            TooltipConfig tooltipConfig = new TooltipConfig();
            tooltipConfig.withPlacement(TooltipConfig.Placement.top);
            TooltipBehavior tooltipBehavior = new TooltipBehavior(
                    Model.of(item.getModelObject().getSingleTender().getTitle()), tooltipConfig);
            titleLabel.add(tooltipBehavior);
        }
        headerFragment.add(titleLabel);
        headerFragment.add(createValidationLabel(item));

        WebMarkupContainer terminatedRequisition = new WebMarkupContainer("terminatedRequisition");

        TenderProcess tenderProcess = tenderProcessService.findById(item.getModelObject().getId()).get();
        terminatedRequisition.setVisibilityAllowed(tenderProcess.isTerminated());
        headerFragment.add(terminatedRequisition);

        header.add(headerFragment);
    }

    protected Label getTenderProcessTitleLabel(int index, TenderProcess tp) {
        if (tp.getSingleTender() != null && !StringUtils.isEmpty(tp.getSingleTender().getTitle())) {
            return new Label("tenderProcessTitle",
                    StringUtils.abbreviate(tp.getSingleTender().getTitle(), MAX_VISIBLE_TENDER_TITLE_LENGTH));
        }
        return new Label("tenderProcessTitle",
                new StringResourceModel("tenderProcessIndexed").setParameters(index));
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

            RepeatingView panels = new RepeatingView("panels");
            containerFragment.add(panels);

            final Tender tender = PersistenceUtil.getNext(tenderProcess.getTender());
            final PurchaseRequisitionGroup purchaseRequisitionGroup = tenderProcess.getSinglePurchaseRequisition();
            final TenderQuotationEvaluation tenderQuotationEvaluation = PersistenceUtil.getNext(tenderProcess
                    .getTenderQuotationEvaluation());
            final ProfessionalOpinion professionalOpinion = PersistenceUtil.getNext(
                    tenderProcess.getProfessionalOpinion());
            final AwardNotification awardNotification = PersistenceUtil.getNext(tenderProcess.getAwardNotification());
            final AwardAcceptance awardAcceptance = PersistenceUtil.getNext(tenderProcess.getAwardAcceptance());
            final Contract contract = PersistenceUtil.getNext(tenderProcess.getContract());

            final Panel requisitionPanel = new TenderDetailPanel<>("requisitionPanel",
                    Collections.singletonList(purchaseRequisitionGroup),
                    pr -> pr != null ? Arrays.asList(
                            pr.getPurchaseRequestNumber(),
                            pr.getPurchRequisitions().stream().map(PurchRequisition::getRequestApprovalDate)
                                    .filter(Objects::nonNull).map(formatter::format).collect(Collectors.toList()),
                            pr.getAmount()
                    ) : null, tenderProcess, EditPurchaseRequisitionGroupPage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, PurchaseRequisitionGroup.class), false,
                    "deptOverview.tenderProcess.purchaseRequisition"
            );
            panels.add(requisitionPanel);

            final Panel tenderPanel = new TenderDetailPanel<>("tenderPanel", Collections.singletonList(tender),
                    t ->
                    t != null ? (Arrays.asList(t.getTenderTitle(), t.getTenderNumber(), t.getTenderValue())) : null,
                    tenderProcess, EditTenderPage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, Tender.class), false,
                    "deptOverview.tenderProcess.tenderDocument");
            panels.add(tenderPanel);

            final Panel evaluationPanel = new TenderDetailPanel<>("evaluationPanel",
                    Collections.singletonList(tenderQuotationEvaluation),
                    qe -> (qe != null && tender != null)
                            ? new ArrayList<>(Arrays.asList(tender.getTenderTitle(), tender.getTenderNumber())) : null,
                    tenderProcess, EditTenderQuotationEvaluationPage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, TenderQuotationEvaluation.class), false,
                    "deptOverview.tenderProcess.tenderQuotationEvaluation");
            panels.add(evaluationPanel);

            final Panel professionalOpinionPanel = new TenderDetailPanel<>("professionalOpinionPanel",
                    Collections.singletonList(professionalOpinion),
                    po -> po != null ? po.getItems()
                            .stream()
                            .map(Objects::toString)
                            .collect(Collectors.toList())
                            : null,
                    tenderProcess, EditProfessionalOpinionPage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, ProfessionalOpinion.class), false,
                    "deptOverview.tenderProcess.professionalOpinion");
            panels.add(professionalOpinionPanel);

            final Panel awardNotificationPanel = new TenderDetailPanel<>("awardNotificationPanel",
                    Collections.singletonList(awardNotification),
                    an -> an != null ? an.getItems()
                            .stream()
                            .map(Objects::toString)
                            .collect(Collectors.toList()) : null,
                    tenderProcess, EditAwardNotificationPage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, AwardNotification.class), false,
                    "deptOverview.tenderProcess.awardNotification");
            panels.add(awardNotificationPanel);

            final Panel awardAcceptancePanel = new TenderDetailPanel<>("awardAcceptancePanel",
                    Collections.singletonList(awardAcceptance),
                    aa -> aa != null ? aa.getItems().stream().map(Objects::toString)
                            .collect(Collectors.toList()) : null,
                    tenderProcess, EditAwardAcceptancePage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, AwardAcceptance.class), false,
                    "deptOverview.tenderProcess.awardAcceptance"
            );
            panels.add(awardAcceptancePanel);

            final Panel contractPanel = new TenderDetailPanel<>("contractPanel", Collections.singletonList(contract),
                    c -> c != null ? new ArrayList<>(Arrays.asList(c.getAwardee(),
                    c.getContractValue())) : null, tenderProcess, EditContractPage.class,
                    tenderProcessService.getPreviousStatusable(tenderProcess, Contract.class), false,
                    "deptOverview.tenderProcess.contract"
            );
            panels.add(contractPanel);

            final Panel administratorReportPanel = new TenderDetailPanel<>("administratorReportPanel",
                    tenderProcess.getAdministratorReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    ar -> Collections.singletonList(ar.getLabel()),
                    tenderProcess, EditAdministratorReportPage.class, contract, true,
                    "deptOverview.tenderProcess.administratorReport"
            );
            panels.add(administratorReportPanel);

            final Panel inspectionReportPanel = new TenderDetailPanel<>("inspectionReportPanel",
                    tenderProcess.getInspectionReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    ir -> Collections.singletonList(ir.getLabel()),
                    tenderProcess, EditInspectionReportPage.class, contract, true,
                    "deptOverview.tenderProcess.inspectionReport"
            );
            panels.add(inspectionReportPanel);

            final Panel pmcReportPanel = new TenderDetailPanel<>("pmcReportPanel",
                    tenderProcess.getPmcReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    pmc -> Collections.singletonList(pmc.getLabel()),
                    tenderProcess, EditPMCReportPage.class, contract, true,
                    "deptOverview.tenderProcess.pmcReport"
            );
            panels.add(pmcReportPanel);

            final Panel meReportPanel = new TenderDetailPanel<>("meReportPanel",
                    tenderProcess.getMeReports().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    me -> Collections.singletonList(me.getLabel()),
                    tenderProcess, EditMEReportPage.class, contract, true,
                    "deptOverview.tenderProcess.meReport"
            );
            panels.add(meReportPanel);

            final Panel paymentVoucherPanel = new TenderDetailPanel<>("paymentVoucherPanel",
                    tenderProcess.getPaymentVouchers().stream().sorted(
                            Comparator.comparingLong(AbstractMakueniEntity::getId)).collect(Collectors.toList()),
                    pv -> Collections.singletonList(pv.getLabel()),
                    tenderProcess, EditPaymentVoucherPage.class, contract, true,
                    "deptOverview.tenderProcess.paymentVoucher"
            );
            panels.add(paymentVoucherPanel);
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
    public boolean canEdit(final TenderProcess tenderProcess2,
                           final AbstractStatusAuditableEntity persistable,
                           final Statusable previousStep, boolean allowNullProjects) {

        TenderProcess tenderProcess = tenderProcessService.findById(tenderProcess2.getId()).get();

        //terminated can always edit
        if (persistable != null && persistable.getStatus().equals(DBConstants.Status.TERMINATED)) {
            return true;
        }

        //the rest of the steps of a terminated chain, can never be edited
        if (persistable == null && tenderProcess.isTerminated()) {
            return false;
        }

        if (persistable != null && persistable instanceof PurchaseRequisitionGroup && allowNullProjects) {
            return true;
        }

        return previousStep != null;
    }

    private class TenderDetailPanel<T extends AbstractMakueniEntity> extends GenericPanel<T> {
        private final List<T> entities;

        private final SerializableFunction<T, List<Object>> tenderInfo;

        private final TenderProcess tenderProcess;

        private final Class<? extends AbstractEditPage<?>> editClazz;
        private final Statusable previousStep;

        private boolean multiple;

        TenderDetailPanel(final String id, final List<T> entities,
                final SerializableFunction<T, List<Object>> tenderInfo, final TenderProcess tenderProcess,
                final Class<? extends AbstractEditPage<?>> editClazz, Statusable previousStep,
                boolean multiple, String featureName) {
            super(id);

            this.multiple = multiple;
            this.entities = entities;
            this.previousStep = previousStep;
            this.tenderInfo = tenderInfo;
            this.tenderProcess = tenderProcess;
            this.editClazz = editClazz;

            add(new DgFmBehavior(featureName));
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

            add(new Label("tenderLabel", new StringResourceModel(getId() + ".tenderLabel")));

            add(new ListView<T>("entities", new ListModel<>(entities)) {
                @Override
                protected void populateItem(final ListItem<T> item) {

                    final T itemObj = item.getModelObject();
                    item.add(new DeptOverviewStatusLabel("tenderStatus", itemObj == null
                            ? null : itemObj.getStatus()));

                    BootstrapAjaxLink<Void> editTender = createEditButton(itemObj);

                    final String buttonType;
                    if (canAccessAddNewButtons(editClazz)) {
                        buttonType = "edit";
                    } else {
                        buttonType = "view";
                    }
                    editTender.add(AttributeAppender.append("class", "no-text btn-" + buttonType));

                    editTender.add(new TooltipBehavior(EditViewResourceModel.of(canAccessAddNewButtons(editClazz),
                            TenderDetailPanel.this.getId() + ".entity", this)));

                    if (item.getModelObject() == null) {
                        editTender.setVisibilityAllowed(canAccessAddNewButtons(editClazz));
                    }

                    editTender.setEnabled(canEdit(tenderProcess, item.getModelObject(), previousStep,
                            allowNullProjects));

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
                    null, previousStep, allowNullProjects));
            addButton.add(AttributeAppender.append("class", "no-text btn-add"));
            add(addButton);
        }
    }
}